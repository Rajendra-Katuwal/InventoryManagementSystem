package controller.customer;

import dao.OrderDAO;
import dao.AuditLogDAO;
import model.Order;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for managing customer orders with pagination and cancellation.
 */
@WebServlet("/customer/orders")
public class CustomerOrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;
    private OrderDAO orderDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        auditLogDAO = new AuditLogDAO();
    }

    /**
     * Handles GET requests to display customer orders with pagination.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Handle pagination
        int currentPage = parsePageParameter(request);
        int userId = user.getUserId();

        try {
            // Fetch orders and total count
            int totalOrders = orderDAO.getOrderCountByUserId(userId);
            List<Order> orders = fetchOrders(userId, currentPage, session);

            // Adjust page if out of bounds
            int totalPages = calculateTotalPages(totalOrders);
            if (currentPage > totalPages) {
                currentPage = totalPages;
                orders = fetchOrders(userId, currentPage, session);
            }

            // Fetch order items and product names
            Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();
            Map<Integer, String> productNamesMap = new HashMap<>();
            fetchOrderDetails(orders, orderItemsMap, productNamesMap, session);

            // Fetch user email
            String userEmail = fetchUserEmail(userId, session);

            // Log order view action
            logAudit(userId, "VIEW_ORDERS", "User viewed orders, page: " + currentPage);

            // Set request attributes
            setRequestAttributes(request, orders, orderItemsMap, productNamesMap, userEmail, currentPage, totalPages);

            // Forward to JSP
            forwardToJsp(request, response);

        } catch (SQLException e) {
            session.setAttribute("error", "Unable to load orders due to a database error.");
            logAudit(userId, "ERROR_VIEW_ORDERS", "Database error fetching orders: " + e.getMessage());
            forwardToJsp(request, response);
        }
    }

    /**
     * Handles POST requests for order cancellation.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        String action = request.getParameter("action");
        if ("cancelOrder".equals(action)) {
            handleCancelOrder(request, response, user);
        } else {
            session.setAttribute("error", "Invalid action specified.");
            logAudit(user.getUserId(), "INVALID_ACTION", "Invalid action: " + action);
            response.sendRedirect(request.getContextPath() + "/customer/orders");
        }
    }


    /**
     * Parses the page parameter, defaulting to 1 if invalid.
     */
    private int parsePageParameter(HttpServletRequest request) {
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                int page = Integer.parseInt(pageParam);
                return page < 1 ? 1 : page;
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("warning", "Invalid page number, defaulting to page 1.");
        }
        return 1;
    }

    /**
     * Fetches orders for the given user and page.
     */
    private List<Order> fetchOrders(int userId, int currentPage, HttpSession session) throws SQLException {
        List<Order> orders = orderDAO.getOrderByUserId(userId, PAGE_SIZE, (currentPage - 1) * PAGE_SIZE);
        if (orders.isEmpty()) {
            session.setAttribute("info", "No orders found.");
        }
        return orders;
    }

    /**
     * Calculates total pages based on order count.
     */
    private int calculateTotalPages(int totalOrders) {
        return Math.max(1, (int) Math.ceil((double) totalOrders / PAGE_SIZE));
    }

    /**
     * Fetches order items and product names.
     */
    private void fetchOrderDetails(List<Order> orders, Map<Integer, List<OrderItem>> orderItemsMap,
                                  Map<Integer, String> productNamesMap, HttpSession session) {
        for (Order order : orders) {
            try {
                List<OrderItem> items = orderDAO.getOrderItems(order.getOrderId());
                orderItemsMap.put(order.getOrderId(), items);
                for (OrderItem item : items) {
                    try {
                        String productName = orderDAO.getProductName(item.getProductId());
                        productNamesMap.put(item.getOrderItemId(), productName != null ? productName : "Unknown Product");
                    } catch (SQLException e) {
                        session.setAttribute("warning", "Could not load some product names.");
                        productNamesMap.put(item.getOrderItemId(), "Unknown Product");
                    }
                }
            } catch (SQLException e) {
                session.setAttribute("warning", "Could not load details for some orders.");
                orderItemsMap.put(order.getOrderId(), new ArrayList<>());
            }
        }
    }

    /**
     * Fetches user email.
     */
    private String fetchUserEmail(int userId, HttpSession session) {
        try {
            String email = orderDAO.getUserEmail(userId);
            return email != null ? email : "Unknown Email";
        } catch (SQLException e) {
            session.setAttribute("warning", "Could not load user email.");
            return "Unknown Email";
        }
    }

    /**
     * Sets request attributes for JSP rendering.
     */
    private void setRequestAttributes(HttpServletRequest request, List<Order> orders,
                                     Map<Integer, List<OrderItem>> orderItemsMap, Map<Integer, String> productNamesMap,
                                     String userEmail, int currentPage, int totalPages) {
        request.setAttribute("orders", orders);
        request.setAttribute("orderItemsMap", orderItemsMap);
        request.setAttribute("productNamesMap", productNamesMap);
        request.setAttribute("userEmail", userEmail);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("pageTitle", "My Orders - Inventoria");
        request.setAttribute("contentPage", "/WEB-INF/views/customer/orders.jsp");
        request.setAttribute("cssFile", "customer-orders");
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
            session.setAttribute("error", "An error occurred while rendering the orders page.");
            logAudit(0, "ERROR_RENDER_JSP", "Failed to forward to JSP: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/orders");
        }
    }

    /**
     * Handles order cancellation.
     */
    private void handleCancelOrder(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        HttpSession session = request.getSession();
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            // Verify order ownership
            if (!orderDAO.isUserOrder(orderId, user.getUserId())) {
                session.setAttribute("error", "You are not authorized to cancel this order.");
                logAudit(user.getUserId(), "UNAUTHORIZED_CANCEL", "Attempted to cancel order ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/customer/orders");
                return;
            }

            // Check order status
            Order order = orderDAO.findById(orderId);
            if (order == null || order.isDeleted()) {
                session.setAttribute("error", "Order not found or already deleted.");
                logAudit(user.getUserId(), "INVALID_ORDER_CANCEL", "Order ID: " + orderId + " not found or deleted");
                response.sendRedirect(request.getContextPath() + "/customer/orders");
                return;
            }

            if (!"pending".equalsIgnoreCase(order.getOrderStatus())) {
                session.setAttribute("error", "Only pending orders can be cancelled.");
                logAudit(user.getUserId(), "INVALID_STATUS_CANCEL", "Attempted to cancel non-pending order ID: " + orderId);
                response.sendRedirect(request.getContextPath() + "/customer/orders");
                return;
            }

            // Soft-delete order
            orderDAO.softDelete(orderId);
            session.setAttribute("success", "Order cancelled successfully.");
            logAudit(user.getUserId(), "ORDER_CANCELLED", "Cancelled order ID: " + orderId);

        } catch (NumberFormatException e) {
            session.setAttribute("error", "Invalid order ID provided.");
            logAudit(user.getUserId(), "INVALID_ORDER_ID", "Invalid order ID format: " + request.getParameter("orderId"));
        } catch (SQLException e) {
            session.setAttribute("error", "Unable to cancel order due to a database error.");
            logAudit(user.getUserId(), "ERROR_CANCEL_ORDER", "Database error cancelling order: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/customer/orders");
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