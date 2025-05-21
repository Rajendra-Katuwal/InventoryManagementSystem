package controller.customer;

import dao.ProductDAO;
import model.Product;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/** Servlet for displaying product details. */
@WebServlet("/customer/product-details")
public class ProductDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            // Validate user
            User user = (User) session.getAttribute("user");
            if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
                session.setAttribute("error", "Customer login required.");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // User info for sidebar
            String fullName = user.getName() != null ? user.getName() : "";
            String initials = fullName.isEmpty() ? "JD" : fullName.substring(0, 1).toUpperCase() +
                    (fullName.contains(" ") ? fullName.substring(fullName.indexOf(" ") + 1, fullName.indexOf(" ") + 2).toUpperCase() : "");
            request.setAttribute("userName", fullName);
            request.setAttribute("userInitials", initials);

            // Parse product ID
            int productId = parseProductId(request.getParameter("productId"));

            // Fetch product
            Product product;
            try {
                product = productDAO.findById(productId);
            } catch (SQLException e) {
                session.setAttribute("error", "Database error loading product details.");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check if product exists
            if (product == null) {
                session.setAttribute("error", "Product not found.");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Set messages and attributes
            session.setAttribute("info", "Viewing details for " + product.getName());
            request.setAttribute("product", product);

            // Forward to JSP
            request.setAttribute("pageTitle", product.getName() + " - Inventoria");
            request.setAttribute("contentPage", "/WEB-INF/views/customer/product-details.jsp");
            request.setAttribute("cssFile", "product-details");
            request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);

        } catch (Exception e) {
            session.setAttribute("error", "Unexpected error loading product details.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private int parseProductId(String productIdStr) throws ServletException {
        try {
            return productIdStr != null ? Integer.parseInt(productIdStr) : -1;
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid product ID.");
        }
    }
}