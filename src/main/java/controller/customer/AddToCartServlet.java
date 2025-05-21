package controller.customer;

import dao.CartDAO;
import dao.AuditLogDAO;
import model.User;
import model.AuditLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/** Servlet for adding products to the customer's cart. */
@WebServlet("/customer/add-to-cart")
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO;
	private AuditLogDAO auditLogDAO;

	@Override
	public void init() throws ServletException {
		cartDAO = new CartDAO();
		auditLogDAO = new AuditLogDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			// Validate user
			User user = (User) session.getAttribute("user");
			if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
				session.setAttribute("error", "Customer login required to add to cart.");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			// Parse and validate inputs
			int productId;
			int quantity;
			try {
				productId = Integer.parseInt(request.getParameter("productId"));
				quantity = Integer.parseInt(request.getParameter("quantity"));
			} catch (NumberFormatException e) {
				session.setAttribute("error", "Invalid product ID or quantity.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			if (productId <= 0 || quantity <= 0) {
				session.setAttribute("error", "Product ID and quantity must be positive.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			// Add to cart
			try {
				cartDAO.addToCart(user.getUserId(), productId, quantity);
			} catch (SQLException e) {
				session.setAttribute("error", "Database error adding to cart.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Log audit
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("CART_ITEM_ADDED");
			log.setDetails("Added product ID " + productId + " with quantity " + quantity + " to cart.");
			try {
				auditLogDAO.create(log);
			} catch (SQLException e) {
				session.setAttribute("error", "Error logging cart addition.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Set success message
			session.setAttribute("success", "Item added to cart successfully.");

			// Redirect to browse products
			response.sendRedirect(request.getContextPath() + "/customer/browse-products");

		} catch (Exception e) {
			session.setAttribute("error", "Unexpected error adding to cart.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}