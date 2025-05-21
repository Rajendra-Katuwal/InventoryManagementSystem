package controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("pageTitle", "About Us - Inventoria");
		request.setAttribute("contentPage", "/WEB-INF/views/common/about.jsp");
		request.setAttribute("cssFile", "about");
		request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);
	}
}