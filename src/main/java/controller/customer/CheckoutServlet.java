package controller.customer;

import dao.CartDAO;
import dao.OrderDAO;
import dao.AuditLogDAO;
import model.Cart;
import model.OrderItem;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for handling customer checkout and order placement.
 */
@WebServlet("/customer/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
        orderDAO = new OrderDAO();
        auditLogDAO = new AuditLogDAO();
    }

    /**
     * Handles GET requests to display the checkout page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getUserId();
        try {
            // Fetch cart items
            List<Cart> cartItems = cartDAO.getCartItems(userId);
            if (cartItems.isEmpty()) {
                session.setAttribute("error", "Your cart is empty. Add items before checking out.");
                logAudit(userId, "EMPTY_CART", "Attempted checkout with empty cart");
                response.sendRedirect(request.getContextPath() + "/customer/cart");
                return;
            }

            // Calculate total amount
            double totalAmount = calculateTotalAmount(cartItems);

            // Log checkout view
            logAudit(userId, "VIEW_CHECKOUT", "User accessed checkout page");

            // Set request attributes
            setRequestAttributes(request, cartItems, totalAmount);

            // Forward to JSP
            forwardToJsp(request, response);

        } catch (SQLException e) {
            session.setAttribute("error", "Unable to load checkout due to a database error.");
            logAudit(userId, "ERROR_VIEW_CHECKOUT", "Database error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/cart");
        }
    }

    /**
     * Handles POST requests to process order placement.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getUserId();
        try {
            // Fetch cart items
            List<Cart> cartItems = cartDAO.getCartItems(userId);
            if (cartItems.isEmpty()) {
                session.setAttribute("error", "Your cart is empty. Add items before checking out.");
                logAudit(userId, "EMPTY_CART", "Attempted order placement with empty cart");
                response.sendRedirect(request.getContextPath() + "/customer/cart");
                return;
            }

            // Validate shipping address
            String shippingAddress = validateShippingAddress(request, session);
            if (shippingAddress == null) {
                logAudit(userId, "INVALID_ADDRESS", "Missing or invalid shipping address");
                response.sendRedirect(request.getContextPath() + "/customer/checkout");
                return;
            }

            // Calculate total amount
            double totalAmount = calculateTotalAmount(cartItems);

            // Create order and order items
            int orderId = createOrder(userId, totalAmount, shippingAddress, cartItems);

            // Clear cart
            clearCart(userId);

            // Log order placement
            logAudit(userId, "ORDER_PLACED", "Order #" + orderId + " placed with total $" + String.format("%.2f", totalAmount));

            // Set success message
            session.setAttribute("success", "Order placed successfully! Order #" + orderId);
            response.sendRedirect(request.getContextPath() + "/customer/orders");

        } catch (SQLException e) {
            session.setAttribute("error", "Unable to place order due to a database error.");
            logAudit(userId, "ERROR_PLACE_ORDER", "Database error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/checkout");
        }
    }


    /**
     * Calculates the total amount from cart items.
     */
    private double calculateTotalAmount(List<Cart> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Validates and returns the shipping address.
     */
    private String validateShippingAddress(HttpServletRequest request, HttpSession session) {
        String shippingAddress = request.getParameter("shippingAddress");
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            session.setAttribute("error", "Shipping address is required.");
            return null;
        }
        if (shippingAddress.length() > 255) {
            session.setAttribute("error", "Shipping address cannot exceed 255 characters.");
            return null;
        }
        return shippingAddress.trim();
    }

    /**
     * Creates an order and its items.
     */
    private int createOrder(int userId, double totalAmount, String shippingAddress, List<Cart> cartItems)
            throws SQLException {
        // Create order
        int orderId = orderDAO.createOrder(userId, totalAmount, shippingAddress);

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getPrice());
            orderItems.add(orderItem);
        }
        orderDAO.addOrderItems(orderId, orderItems);

        return orderId;
    }

    /**
     * Clears the user's cart.
     */
    private void clearCart(int userId) throws SQLException {
        cartDAO.clearCart(userId);
    }

    /**
     * Sets request attributes for JSP rendering.
     */
    private void setRequestAttributes(HttpServletRequest request, List<Cart> cartItems, double totalAmount) {
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("pageTitle", "Checkout - Inventoria");
        request.setAttribute("contentPage", "/WEB-INF/views/customer/checkout.jsp");
        request.setAttribute("cssFile", "checkout");
    }

    /**
     * Forwards request to JSP template.
     */
    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "An error occurred while rendering the checkout page.");
            logAudit(0, "ERROR_RENDER_JSP", "Failed to forward to JSP: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/cart");
        }
    }

    /**
     * Logs an audit entry.
     */
    private void logAudit(int userId, String action, String details) {
        try {
            auditLogDAO.create(new AuditLog(userId, action, details));
        } catch (SQLException e) {
            // Suppress audit logging errors to avoid disrupting main flow
        }
    }
}