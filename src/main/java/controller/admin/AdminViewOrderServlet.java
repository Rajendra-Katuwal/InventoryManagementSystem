package controller.admin;

import dao.OrderDAO;
import dao.UserDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.AuditLogDAO;
import model.Order;
import model.OrderItem;
import model.User;
import model.Product;
import model.AuditLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for viewing order details in the admin panel.
 */
@WebServlet("/admin/viewOrder")
public class AdminViewOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private OrderItemDAO orderItemDAO;
    private ProductDAO productDAO;
    private AuditLogDAO auditLogDAO;

    /**
     * Initializes DAO instances.
     */
    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        orderItemDAO = new OrderItemDAO();
        productDAO = new ProductDAO();
        auditLogDAO = new AuditLogDAO();
    }

    /**
     * Handles GET requests to display order details.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Validate admin access
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            session.setAttribute("error", "You must be logged in as an admin to view orders.");
            redirectWithState(request, response);
            return;
        }

        int userId = user.getUserId();
        try {
            // Parse order ID
            int orderId = parseOrderId(request.getParameter("id"));
            Order order = orderDAO.findById(orderId);

            if (order == null) {
                session.setAttribute("error", "Order not found.");
                redirectWithState(request, response);
                return;
            }

            // Fetch order items and product names
            List<OrderItem> orderItems = orderItemDAO.findByOrderId(orderId);
            User orderUser = userDAO.findById(order.getUserId());
            String userEmail = orderUser != null && orderUser.getEmail() != null ? orderUser.getEmail() : "Unknown";

            Map<Integer, String> productNames = new HashMap<>();
            for (OrderItem item : orderItems) {
                Product product = productDAO.findById(item.getProductId());
                productNames.put(item.getOrderItemId(), product != null && product.getName() != null ? product.getName() : "Unknown Product");
            }

            // Load orders for the main page
            String searchQuery = request.getParameter("search");
            int page = 1;
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                        session.setAttribute("warning", "Invalid page number, defaulting to page 1.");
                    }
                }
            } catch (NumberFormatException e) {
                session.setAttribute("warning", "Invalid page number, defaulting to page 1.");
                page = 1;
            }

            List<Order> orders;
            int totalOrders;
            int totalPages;

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                orders = orderDAO.searchOrders(searchQuery, page, PAGE_SIZE);
                totalOrders = orderDAO.getTotalSearchOrders(searchQuery);
            } else {
                orders = orderDAO.getOrders(page, PAGE_SIZE);
                totalOrders = orderDAO.getTotalOrders();
            }

            Map<Integer, String> userEmails = new HashMap<>();
            for (Order o : orders) {
                try {
                    User u = userDAO.findById(o.getUserId());
                    userEmails.put(o.getOrderId(), u != null && u.getEmail() != null ? u.getEmail() : "Unknown");
                } catch (SQLException e) {
                    userEmails.put(o.getOrderId(), "Unknown");
                    session.setAttribute("warning", "Could not load email for some orders.");
                }
            }

            totalPages = Math.max(1, (int) Math.ceil((double) totalOrders / PAGE_SIZE));

            // Log order view
            try {
                auditLogDAO.create(new AuditLog(userId, "VIEW_ORDER", "Admin viewed details for order ID: " + orderId));
            } catch (SQLException ignored) {
                // Suppress audit logging errors
            }

            // Set request attributes
            request.setAttribute("order", order);
            request.setAttribute("userEmail", userEmail);
            request.setAttribute("orderItems", orderItems);
            request.setAttribute("productNames", productNames);
            request.setAttribute("showModal", "view-order-modal");
            request.setAttribute("orders", orders);
            request.setAttribute("userEmails", userEmails);
            request.setAttribute("notifications", productDAO.getLowStockItems() + orderDAO.getPendingShipments());
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("pageTitle", "Order Management - Inventoria");
            request.setAttribute("contentPage", "/WEB-INF/views/admin/orders.jsp");
            request.setAttribute("cssFile", "admin-orders");

            // Forward to JSP
            try {
                request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                session.setAttribute("error", "Failed to render the order details page.");
                redirectWithState(request, response);
            }
        } catch (SQLException e) {
            session.setAttribute("error", "Unable to load order details due to a database error.");
            redirectWithState(request, response);
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            redirectWithState(request, response);
        }
    }

    /**
     * Parses and validates the order ID from a string.
     */
    private int parseOrderId(String idStr) throws IllegalArgumentException {
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid order ID");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid order ID format");
        }
    }

    /**
     * Redirects to the orders page, preserving pagination and search state.
     */
    private void redirectWithState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = request.getParameter("page") != null ? request.getParameter("page") : "1";
        String search = request.getParameter("search") != null ? request.getParameter("search") : "";
        response.sendRedirect(request.getContextPath() + "/admin/orders?page=" + page + "&search=" + search);
    }
}