package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter(filterName = "AuthRoleFilter", urlPatterns = {"/admin/*", "/user/*"})
public class AuthRoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        
        String ctx = request.getContextPath();

        // Checks if the session is valid and has user
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            String role = null;
            try {
                role = user.getRole();
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(ctx + "/login");
                return;
            }

            String path = request.getRequestURI();

            // Check if user is accessing allowed path based on their role
            if (path.startsWith(ctx + "/admin") && !"admin".equals(role)) {
                response.sendRedirect(ctx + "/error?code=403"); // Show unauthorized page
            } else if (path.startsWith(ctx + "/user") && !"user".equals(role)) {
                response.sendRedirect(ctx + "/error?code=403"); // Show unauthorized page
            } else {
                filterChain.doFilter(request, response); // Allow access
            }

        } else {
            response.sendRedirect(ctx + "/login"); // Redirect if session is invalid
        }
    }
}
