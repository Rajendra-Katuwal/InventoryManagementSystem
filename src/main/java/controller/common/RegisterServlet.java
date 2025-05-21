package controller.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.UserDAO;
import model.User;
import util.PasswordHasher;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("pageTitle", "Register");
		request.setAttribute("contentPage", "/WEB-INF/views/common/register.jsp");
		request.setAttribute("cssFile", "register");
		request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirm-password");
		String contactPhone = request.getParameter("phone-number");
		String role = request.getParameter("role");
		
		String name = firstname + " " + lastname;

		// Validate name (at least two words)
		if (name == null || name.trim().split("\\s+").length < 2) {
			System.out.print("full name: " + name);
			request.getSession().setAttribute("error", "Please enter your full name (first and last name).");
			response.sendRedirect(
					request.getContextPath() + "/register?name=" + name + "&email=" + email + "&role=" + role);
			return;
		}

		// Validate passwords match
		if (!password.equals(confirmPassword)) {
			request.getSession().setAttribute("error", "Passwords do not match.");
			response.sendRedirect(
					request.getContextPath() + "/register?name=" + name + "&email=" + email + "&role=" + role);
			return;
		}

		// Default role to "customer" if not provided
		if (role == null || role.isEmpty()) {
			role = "customer";
		}

		try {
			if (userDAO.findByEmail(email) != null) {
				request.getSession().setAttribute("error", "Email already exists.");
				response.sendRedirect(
						request.getContextPath() + "/register?name=" + name + "&email=" + email + "&role=" + role);
				return;
			}

			String hashedPassword = PasswordHasher.hashPassword(password);
			User user = new User();
			user.setName(name);
			user.setEmail(email);
			user.setPassword(hashedPassword);
			user.setContactPhone(contactPhone);
			user.setRole(role);

			userDAO.createCustomer(user);
			request.getSession().setAttribute("success", "Registration successful! Please log in.");
			response.sendRedirect(request.getContextPath() + "/login");
		} catch (Exception e) {
			request.getSession().setAttribute("error", "An error occurred during registration. Please try again.");
			response.sendRedirect(
					request.getContextPath() + "/register?name=" + name + "&email=" + email + "&role=" + role);
		}
	}
}