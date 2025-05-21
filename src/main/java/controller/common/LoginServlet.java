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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is already logged in, redirect them to the appropriate dashboard
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			if (user.getRole().equals("admin")) {
				response.sendRedirect(request.getContextPath() + "/admin/dashboard");
			} else {
				response.sendRedirect(request.getContextPath() + "/customer/dashboard");
			}
			return;
		}

		// Otherwise, show the login page
		request.setAttribute("pageTitle", "Login - Inventoria");
		request.setAttribute("contentPage", "/WEB-INF/views/common/login.jsp");
		request.setAttribute("cssFile", "login");
		request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		try {
			User user = userDAO.findByEmail(email);
			if (user != null && PasswordHasher.verifyPassword(password, user.getPassword())) {
				request.getSession().setAttribute("user", user);
				if (user.getRole().equals("admin")) {
					request.getSession().setAttribute("success", "Logged in successfully.");
					response.sendRedirect(request.getContextPath() + "/admin/dashboard");
				} else {
					request.getSession().setAttribute("success", "Logged in successfully.");
					response.sendRedirect(request.getContextPath() + "/customer/dashboard");
				}
			} else {
				request.getSession().setAttribute("error", "Invalid email or password");
				response.sendRedirect(request.getContextPath() + "/login?email=" + email);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging
			request.getSession().setAttribute("error", "An error occurred during login. Please try again.");
			response.sendRedirect(request.getContextPath() + "/login?email=" + email);
		}
	}
}