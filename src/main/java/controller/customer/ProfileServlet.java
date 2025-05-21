package controller.customer;

import dao.UserDAO;
import dao.AuditLogDAO;
import model.User;
import model.AuditLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

/** Servlet for managing customer profile and password updates. */
@WebServlet("/customer/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
		maxFileSize = 1024 * 1024 * 2, // 2MB
		maxRequestSize = 1024 * 1024 * 5 // 5MB
)
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	private AuditLogDAO auditLogDAO;
	private static final String UPLOAD_DIR = "uploads/profiles";

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
		auditLogDAO = new AuditLogDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			// Validate user
			User user = (User) session.getAttribute("user");

			// Refresh user data
			User updatedUser;
			try {
				updatedUser = userDAO.findById(user.getUserId());
			} catch (SQLException e) {
				session.setAttribute("error", "Database error loading profile.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			if (updatedUser == null) {
				session.setAttribute("error", "User not found.");
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			session.setAttribute("user", updatedUser);

			// Set messages and forward
			session.setAttribute("info", "Viewing your profile.");
			request.setAttribute("pageTitle", "My Profile - Inventoria");
			request.setAttribute("contentPage", "/WEB-INF/views/customer/profile.jsp");
			request.setAttribute("cssFile", "profile");
			request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);

		} catch (Exception e) {
			session.setAttribute("error", "Unexpected error loading profile.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			// Validate user
			User user = (User) session.getAttribute("user");
			if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
				session.setAttribute("error", "Customer login required.");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			String action = request.getParameter("action");
			if (action == null) {
				session.setAttribute("error", "Invalid action.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			if ("updateProfile".equals(action)) {
				handleUpdateProfile(request, response, user);
			} else if ("changePassword".equals(action)) {
				handleChangePassword(request, response, user);
			} else {
				session.setAttribute("error", "Invalid action.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			session.setAttribute("error", "Unexpected error updating profile.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		try {
			// Validate inputs
			String email = request.getParameter("email");
			String contactPhone = request.getParameter("contactPhone");
			Part filePart = request.getPart("profilePicture");
			String profilePicPath = user.getProfilePic();

			if (email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				session.setAttribute("error", "Valid email is required.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}
			if (contactPhone == null || contactPhone.trim().isEmpty() || !contactPhone.matches("^[0-9]{10,15}$")) {
				session.setAttribute("error", "Valid contact phone (10-15 digits) is required.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}

			// Handle file upload
			if (filePart != null && filePart.getSize() > 0) {
				String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
				String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
				if (!fileExt.matches("\\.(jpg|jpeg|png|gif)$")) {
					session.setAttribute("error", "Only JPG, PNG, or GIF files are allowed.");
					response.sendRedirect(request.getContextPath() + "/customer/profile");
					return;
				}

				String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				String newFileName = "user_" + user.getUserId() + "_" + System.currentTimeMillis() + fileExt;
				String filePath = uploadPath + File.separator + newFileName;
				filePart.write(filePath);
				profilePicPath = "/" + UPLOAD_DIR + "/" + newFileName;
			}

			// Update profile
			try {
				userDAO.updateProfile(user.getUserId(), email, contactPhone, profilePicPath);
			} catch (SQLException e) {
				session.setAttribute("error", "Database error updating profile.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Refresh user
			User updatedUser;
			try {
				updatedUser = userDAO.findById(user.getUserId());
			} catch (SQLException e) {
				session.setAttribute("error", "Database error refreshing profile.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			if (updatedUser != null) {
				session.setAttribute("user", updatedUser);
			}

			// Log audit
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("PROFILE_UPDATED");
			log.setDetails("User updated profile: email=" + email + ", contactPhone=" + contactPhone
					+ (profilePicPath != null && !profilePicPath.equals(user.getProfilePic())
							? ", profilePic=" + profilePicPath
							: ""));
			try {
				auditLogDAO.create(log);
			} catch (SQLException e) {
				session.setAttribute("error", "Error logging profile update.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			session.setAttribute("success", "Profile updated successfully.");
			response.sendRedirect(request.getContextPath() + "/customer/profile");

		} catch (IOException | ServletException e) {
			session.setAttribute("error", "Error processing profile update.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException {
		HttpSession session = request.getSession();
		try {
			// Validate inputs
			String currentPassword = request.getParameter("currentPassword");
			String newPassword = request.getParameter("newPassword");
			String confirmPassword = request.getParameter("confirmPassword");

			if (currentPassword == null || currentPassword.trim().isEmpty()) {
				session.setAttribute("error", "Current password is required.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}
			if (newPassword == null || newPassword.trim().isEmpty()) {
				session.setAttribute("error", "New password is required.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}
			if (!newPassword.equals(confirmPassword)) {
				session.setAttribute("error", "New password and confirmation do not match.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}
			if (newPassword.length() < 8) {
				session.setAttribute("error", "New password must be at least 8 characters long.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}

			// Update password
			boolean success;
			try {
				success = userDAO.updatePassword(user.getUserId(), currentPassword, newPassword);
			} catch (SQLException e) {
				session.setAttribute("error", "Database error updating password.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			if (!success) {
				session.setAttribute("error", "Current password is incorrect.");
				response.sendRedirect(request.getContextPath() + "/customer/profile");
				return;
			}

			// Log audit
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("PASSWORD_CHANGED");
			log.setDetails("User changed their password.");
			try {
				auditLogDAO.create(log);
			} catch (SQLException e) {
				session.setAttribute("error", "Error logging password change.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			session.setAttribute("success", "Password updated successfully.");
			response.sendRedirect(request.getContextPath() + "/customer/profile");

		} catch (IOException e) {
			session.setAttribute("error", "Error processing password change.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}