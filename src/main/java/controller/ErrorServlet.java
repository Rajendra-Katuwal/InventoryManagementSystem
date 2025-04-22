package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get the error status code
        Integer statusCode = Integer.parseInt(request.getParameter("code"));
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        // Set error information as request attributes
        request.setAttribute("errorCode", statusCode);
        request.setAttribute("errorUri", requestUri);

        String errorPage;
        switch (statusCode) {
            case 401:
                errorPage = "/WEB-INF/views/errors/unauthorized.jsp";
                request.setAttribute("errorMessage", "You are not authorized to access this resource");
                break;
            case 403:
                errorPage = "/WEB-INF/views/errors/forbidden.jsp";
                request.setAttribute("errorMessage", "Access to this resource is forbidden");
                break;
            case 404:
                errorPage = "/WEB-INF/views/errors/not-found.jsp";
                request.setAttribute("errorMessage", "The requested resource was not found");
                break;
            default:
                errorPage = "/WEB-INF/views/errors/internal-error.jsp";
                request.setAttribute("errorMessage", "An internal server error occurred");
                break;
        }

        // Forward to the appropriate error page
        request.getRequestDispatcher(errorPage).forward(request, response);
    }
}