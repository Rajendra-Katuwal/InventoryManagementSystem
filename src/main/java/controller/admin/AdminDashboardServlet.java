package controller.admin;

import dao.ProductDAO;
import dao.OrderDAO;
import dao.AuditLogDAO;
import dao.CategoryDAO;
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
import java.util.List;

/**
 * Servlet for displaying the admin dashboard with key metrics and recent activities.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private AuditLogDAO auditLogDAO;
    private CategoryDAO categoryDAO;

    /**
     * Initializes DAO instances.
     */
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
        orderDAO = new OrderDAO();
        auditLogDAO = new AuditLogDAO();
        categoryDAO = new CategoryDAO();
    }

    /**
     * Handles GET requests to display the admin dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Fetch stats
            int totalProducts = productDAO.getTotalProducts();
            int lowStockItems = productDAO.getLowStockItems();
            int totalOrders = orderDAO.getTotalOrders();
            int pendingShipments = orderDAO.getPendingShipments();

            // Mock percentage changes as Doubles
            double totalProductsChange = 5.3;
            double lowStockItemsChange = 2.8;
            double totalOrdersChange = 12.7;
            double pendingShipmentsChange = 3.6;

            // Fetch recent audit logs
            List<AuditLog> recentLogs = auditLogDAO.getAuditLogs(1, 5, null, null, null, null, 5);

            // Fetch low stock products and add category names
            List<Product> lowStockProducts = productDAO.getLowStockProducts();
            for (Product product : lowStockProducts) {
                String categoryName = categoryDAO.getCategoryName(product.getCategoryId());
                product.setCategoryName(categoryName != null ? categoryName : "Unknown");
            }

            // Calculate notifications
            int notifications = lowStockItems + pendingShipments;

            // Fetch revenue data for the chart
            String period = request.getParameter("period");
            if (period == null || period.trim().isEmpty()) {
                period = "monthly"; // Default to monthly view
            }
            List<OrderDAO.RevenueEntry> revenueData = orderDAO.getRevenueData(period);

            // Set attributes
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("lowStockItems", lowStockItems);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("pendingShipments", pendingShipments);
            request.setAttribute("totalProductsChange", totalProductsChange);
            request.setAttribute("lowStockItemsChange", lowStockItemsChange);
            request.setAttribute("totalOrdersChange", totalOrdersChange);
            request.setAttribute("pendingShipmentsChange", pendingShipmentsChange);
            request.setAttribute("notifications", notifications);
            request.setAttribute("recentLogs", recentLogs);
            request.setAttribute("lowStockProducts", lowStockProducts);
            request.setAttribute("revenueData", revenueData);
            request.setAttribute("selectedPeriod", period);
            request.setAttribute("pageTitle", "Admin Dashboard - Inventoria");
            request.setAttribute("contentPage", "/WEB-INF/views/admin/dashboard.jsp");
            request.setAttribute("cssFile", "admin-dashboard");

            // Forward to JSP
            try {
                request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                session.setAttribute("error", "Failed to render the dashboard page.");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            }

        } catch (SQLException e) {
            session.setAttribute("error", "Unable to load dashboard due to a database error.");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }

    /**
     * Handles POST requests by delegating to doGet for period selection.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}