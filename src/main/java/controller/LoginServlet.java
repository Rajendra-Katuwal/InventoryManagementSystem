package controller;

import dao.UserDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }

        // Check for remembered email cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userCookieEmail".equals(cookie.getName())) {
                    request.setAttribute("userCookieEmail", cookie.getValue());
                    break;
                }
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        try {
            // Input validation
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            	System.out.println("empty error");
            	System.out.println("Email: " + email);
            	System.out.println("Passworrd: " + password);
            	System.out.println("Remember: " + rememberMe);
                request.setAttribute("error", "Email and password are required");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }

            // Authenticate user (Ensure passwords are hashed using bcrypt or similar in UserDAO)
            User user = userDAO.authenticate(email, password);

            if (user != null) {
                // Session Fixation Protection
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("user", user);

                // Handle Remember Me functionality
                handleRememberMe(request, response, email, rememberMe);

                // Redirect based on role
                String contextPath = request.getContextPath();
                if (user.isAdmin() || "admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(contextPath + "/admin/dashboard");
                } else if (user.isManager()) {
                    response.sendRedirect(contextPath + "/manager/dashboard");
                } else {
                    response.sendRedirect(contextPath + "/user/dashboard");
                }
            } else {
            	System.out.println("Invalid email or password");
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
        	System.out.println("An internal error occurred. Please try again.");
            request.setAttribute("error", "An internal error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void handleRememberMe(HttpServletRequest request, HttpServletResponse response, String email, String rememberMe) {
        Cookie emailCookie;
        if ("on".equals(rememberMe)) {
            emailCookie = new Cookie("userCookieEmail", email);
            emailCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
            emailCookie.setHttpOnly(true); // Prevent JavaScript access
            emailCookie.setPath(request.getContextPath());

            // Only set Secure flag if running over HTTPS
            if (request.isSecure()) {
                emailCookie.setSecure(true);
            }
        } else {
            emailCookie = new Cookie("userCookieEmail", "");
            emailCookie.setMaxAge(0); // Delete cookie
            emailCookie.setPath(request.getContextPath());
        }

        response.addCookie(emailCookie);
    }
}
