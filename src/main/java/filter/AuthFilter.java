package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "/admin/*", "/customer/*" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession(false); // Avoid creating new session
		String ctx = request.getContextPath();

		if (session != null && session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			String role = user.getRole();
			String path = request.getRequestURI();

			// Restrict /admin paths to admin role
			if (path.startsWith(ctx + "/admin") && !"admin".equalsIgnoreCase(role)) {
				session.setAttribute("error", "Access denied: Admin role required");
				response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
				// Restrict /customer paths to customer role
			} else if (path.startsWith(ctx + "/customer") && !"customer".equalsIgnoreCase(role)) {
				session.setAttribute("error", "Access denied: Customer role required");
				response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403
			} else {
				filterChain.doFilter(request, response); // Proceed with request
			}
		} else {
			// Set warning for unauthorized access and redirect to login
			HttpSession newSession = request.getSession(true); // Create session for notification
			newSession.setAttribute("warning", "Unauthorized Access");
			response.sendRedirect(ctx + "/login");
		}
	}
}