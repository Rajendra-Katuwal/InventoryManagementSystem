package controller.customer;

import dao.CartDAO;
import dao.AuditLogDAO;
import model.Cart;
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
import java.util.List;

/** Servlet for managing the customer's shopping cart. */
@WebServlet("/customer/cart")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO;
	private AuditLogDAO auditLogDAO;

	@Override
	public void init() throws ServletException {
		cartDAO = new CartDAO();
		auditLogDAO = new AuditLogDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			// Validate user
			User user = (User) session.getAttribute("user");

			// Fetch cart items
			List<Cart> cartItems;
			try {
				cartItems = cartDAO.getCartItems(user.getUserId());
			} catch (SQLException e) {
				session.setAttribute("error", "Database error loading cart.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Calculate total
			double totalAmount = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

			// Set attributes
			request.setAttribute("cartItems", cartItems);
			request.setAttribute("totalAmount", totalAmount);
			request.setAttribute("pageTitle", "Shopping Cart - Inventoria");
			request.setAttribute("contentPage", "/WEB-INF/views/customer/cart.jsp");
			request.setAttribute("cssFile", "cart");

			// Forward to JSP
			request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);

		} catch (Exception e) {
			session.setAttribute("error", "Unexpected error loading cart.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			// Validate user
			User user = (User) session.getAttribute("user");
			if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
				session.setAttribute("error", "Customer login required to modify cart.");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			String action = request.getParameter("action");
			if (action == null) {
				session.setAttribute("error", "Invalid action.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			switch (action) {
			case "add":
				handleAddToCart(request, response, user);
				break;
			case "update":
				handleUpdateCart(request, response, user);
				break;
			case "remove":
				handleRemoveFromCart(request, response, user);
				break;
			case "clear":
				handleClearCart(request, response, user);
				break;
			default:
				session.setAttribute("error", "Invalid action.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			session.setAttribute("error", "Unexpected error processing cart action.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException {
		HttpSession session = request.getSession();
		try {
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

			session.setAttribute("success", "Item added to cart successfully.");
			response.sendRedirect(request.getContextPath() + "/customer/cart");

		} catch (IOException e) {
			session.setAttribute("error", "Error processing cart addition.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException {
		HttpSession session = request.getSession();
		try {
			int cartId;
			int newQuantity;
			try {
				cartId = Integer.parseInt(request.getParameter("cartId"));
				newQuantity = Integer.parseInt(request.getParameter("quantity"));
			} catch (NumberFormatException e) {
				session.setAttribute("error", "Invalid cart ID or quantity.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			if (cartId <= 0) {
				session.setAttribute("error", "Invalid cart ID.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			try {
				if (newQuantity <= 0) {
					cartDAO.removeFromCart(cartId);
					session.setAttribute("success", "Item removed from cart.");

					// Log audit
					AuditLog log = new AuditLog();
					log.setUserId(user.getUserId());
					log.setAction("CART_ITEM_REMOVED");
					log.setDetails("Removed cart item ID " + cartId + " due to zero quantity.");
					auditLogDAO.create(log);
				} else {
					cartDAO.updateCartItem(cartId, newQuantity);
					session.setAttribute("success", "Cart updated successfully.");

					// Log audit
					AuditLog log = new AuditLog();
					log.setUserId(user.getUserId());
					log.setAction("CART_ITEM_UPDATED");
					log.setDetails("Updated cart item ID " + cartId + " to quantity " + newQuantity + ".");
					auditLogDAO.create(log);
				}
			} catch (SQLException e) {
				session.setAttribute("error", "Database error updating cart.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			response.sendRedirect(request.getContextPath() + "/customer/cart");

		} catch (IOException e) {
			session.setAttribute("error", "Error processing cart update.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException {
		HttpSession session = request.getSession();
		try {
			int cartId;
			try {
				cartId = Integer.parseInt(request.getParameter("cartId"));
			} catch (NumberFormatException e) {
				session.setAttribute("error", "Invalid cart ID.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			if (cartId <= 0) {
				session.setAttribute("error", "Invalid cart ID.");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			try {
				cartDAO.removeFromCart(cartId);
			} catch (SQLException e) {
				session.setAttribute("error", "Database error removing from cart.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Log audit
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("CART_ITEM_REMOVED");
			log.setDetails("Removed cart item ID " + cartId + ".");
			try {
				auditLogDAO.create(log);
			} catch (SQLException e) {
				session.setAttribute("error", "Error logging cart removal.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			session.setAttribute("success", "Item removed from cart.");
			response.sendRedirect(request.getContextPath() + "/customer/cart");

		} catch (IOException e) {
			session.setAttribute("error", "Error processing cart removal.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void handleClearCart(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException {
		HttpSession session = request.getSession();
		try {
			try {
				cartDAO.clearCart(user.getUserId());
			} catch (SQLException e) {
				session.setAttribute("error", "Database error clearing cart.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Log audit
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("CART_CLEARED");
			log.setDetails("Cleared all items from cart.");
			try {
				auditLogDAO.create(log);
			} catch (SQLException e) {
				session.setAttribute("error", "Error logging cart clear.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			session.setAttribute("success", "Cart cleared successfully.");
			response.sendRedirect(request.getContextPath() + "/customer/cart");

		} catch (IOException e) {
			session.setAttribute("error", "Error processing cart clear.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}