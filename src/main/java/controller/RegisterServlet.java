package controller;

import dao.UserDAO;
import model.User;
import util.PasswordSecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
//        String phoneNumber = request.getParameter("phonenumber");

        // Simple validation
        if (firstname == null || lastname == null || email == null || password == null ||
            firstname.trim().isEmpty() || firstname.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {

            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        try {
            // Check if user already exists
            boolean userExists = UserDAO.isEmailExists(email); // Checking if a user with this email already exists

            if (userExists) {
                request.setAttribute("error", "An account with this email already exists.");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                return;
            }

            // Save new user
            String encryptedPassword = PasswordSecurityUtil.encryptPassword(password);
            User newUser = new User(firstname, lastname, email, encryptedPassword); 
            boolean isCreated = UserDAO.createUser(newUser);

            if (isCreated) {
                // Redirect to login with success flag
                response.sendRedirect(request.getContextPath() + "/login?registered=true");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
