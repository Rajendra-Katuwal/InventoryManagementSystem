package controller.customer;

import dao.OrderDAO;
import dao.AuditLogDAO;
import model.Order;
import model.AuditLog;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/customer/dashboard")
public class CustomerDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        auditLogDAO = new AuditLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Validate user is a logged-in customer
        if (user == null || !"customer".equalsIgnoreCase(user.getRole())) {
            session.setAttribute("error", "Please log in as a customer to access the dashboard.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Fetch stats
            int userId = user.getUserId();
            double totalOrders = orderDAO.getTotalSpentForUser(userId);
            int pendingOrders = orderDAO.getPendingOrdersForUser(userId);
            int inTransitShipments = orderDAO.getInTransitShipmentsForUser(userId);
            double totalSpent = orderDAO.getTotalSpentForUser(userId);

            // Mock percentage changes
            double totalOrdersChange = 12.7;
            double pendingOrdersChange = -2.8;
            double inTransitChange = 3.6;
            double totalSpentChange = 5.3;

            // Fetch recent orders
            List<Order> recentOrders = orderDAO.getOrderByUserId(userId, 4, 0);

            // Fetch recent activity
            List<AuditLog> recentLogs = auditLogDAO.findByUserId(userId);
            List<Activity> recentActivity = new ArrayList<>();
            for (AuditLog log : recentLogs) {
                Activity activity = new Activity();
                activity.setMessage(log.getDetails());
                activity.setTime(calculateTimeAgo(log.getCreatedAt().toString()));
                activity.setIconClass(getIconClassForAction(log.getAction()));
                recentActivity.add(activity);
            }

            // Calculate notifications
            int notifications = pendingOrders + inTransitShipments;

            // Set dashboard attributes
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("pendingOrders", pendingOrders);
            request.setAttribute("inTransitShipments", inTransitShipments);
            request.setAttribute("totalSpent", totalSpent);
            request.setAttribute("totalOrdersChange", totalOrdersChange);
            request.setAttribute("pendingOrdersChange", pendingOrdersChange);
            request.setAttribute("inTransitChange", inTransitChange);
            request.setAttribute("totalSpentChange", totalSpentChange);
            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("recentActivity", recentActivity);
            request.setAttribute("notifications", notifications);

            // Set info notification for pending/in-transit orders
            if (notifications > 0) {
                request.setAttribute("info", "You have " + notifications + " pending or in-transit orders.");
            }

            // Forward to dashboard template
            request.setAttribute("pageTitle", "Customer Dashboard - Inventoria");
            request.setAttribute("contentPage", "/WEB-INF/views/customer/dashboard.jsp");
            request.setAttribute("cssFile", "customer-dashboard");
            request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);
        } catch (Exception e) {
            // Redirect to error page with error notification
            session.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Get icon class for audit log action
    private String getIconClassForAction(String action) {
        switch (action) {
            case "SHIPMENT_RECEIVED":
                return "truck";
            case "ORDER_PROCESSED":
                return "box";
            case "LOW_STOCK_ALERT":
                return "exclamation-triangle";
            case "INVOICE_GENERATED":
                return "file-invoice";
            default:
                return "info-circle";
        }
    }

    // Calculate time ago for activity
    private String calculateTimeAgo(String createdAt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime createdDateTime = LocalDateTime.parse(createdAt, formatter);
            LocalDateTime now = LocalDateTime.now();

            long seconds = java.time.Duration.between(createdDateTime, now).getSeconds();
            if (seconds < 60)
                return seconds + " seconds ago";
            long minutes = seconds / 60;
            if (minutes < 60)
                return minutes + " minutes ago";
            long hours = minutes / 60;
            if (hours < 24)
                return hours + " hours ago";
            long days = hours / 24;
            if (days == 1)
                return "Yesterday";
            return days + " days ago";
        } catch (Exception e) {
            return "Unknown time";
        }
    }

    // Activity class for recent activity display
    public static class Activity {
        private String message;
        private String time;
        private String iconClass;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIconClass() {
            return iconClass;
        }

        public void setIconClass(String iconClass) {
            this.iconClass = iconClass;
        }
    }
}