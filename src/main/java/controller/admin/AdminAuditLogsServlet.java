package controller.admin;

import dao.UserDAO;
import dao.OrderDAO;
import dao.ProductDAO;
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
import java.util.List;

/**
 * Servlet for displaying paginated audit logs in the admin dashboard with filters for admin, action, and date range.
 */
@WebServlet("/admin/audit-logs")
public class AdminAuditLogsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;

    private AuditLogDAO auditLogDAO;
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    /**
     * Initializes DAO instances.
     */
    @Override
    public void init() throws ServletException {
        auditLogDAO = new AuditLogDAO();
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
    }

    /**
     * Handles GET requests to display the audit logs page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Get filter parameters
            Integer userId = parseUserId(request);
            String action = request.getParameter("action");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");

            // Get pagination parameter
            Integer page = parsePageNumber(request);

            // Fetch audit logs
            List<AuditLog> auditLogs = auditLogDAO.getAuditLogs(page, PAGE_SIZE, userId, action, startDate, endDate, PAGE_SIZE);
            Integer totalLogs = auditLogDAO.getTotalAuditLogs(userId, action, startDate, endDate);
            Integer totalPages = calculateTotalPages(totalLogs);

            // Fetch admins for filter dropdown
            List<User> admins = userDAO.getAdmins();

            // Fetch notifications
            Integer notificationCount = calculateNotifications();

            // Set request attributes
            request.setAttribute("auditLogs", auditLogs);
            request.setAttribute("admins", admins);
            request.setAttribute("notificationCount", notificationCount);
            request.setAttribute("userId", userId);
            request.setAttribute("action", action);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalLogs", totalLogs);
            request.setAttribute("pageTitle", "Audit Logs - Inventoria");
            request.setAttribute("contentPage", "/WEB-INF/views/admin/audit-logs.jsp");
            request.setAttribute("cssFile", "admin-audit-logs");

            // Forward to JSP
            try {
                request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                session.setAttribute("error", "Failed to render the audit logs page.");
                response.sendRedirect(request.getContextPath() + "/admin/audit-logs");
            }
        } catch (SQLException e) {
            session.setAttribute("error", "Unable to load audit logs due to a database error.");
            response.sendRedirect(request.getContextPath() + "/admin/audit-logs");
        }
    }

    /**
     * Parses the user ID filter parameter.
     */
    private Integer parseUserId(HttpServletRequest request) {
        String userIdParam = request.getParameter("userId");
        if (userIdParam != null && !userIdParam.trim().isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdParam);
                if (userId <= 0) {
                    request.getSession().setAttribute("warning", "Invalid admin ID provided.");
                    return null;
                }
                return userId;
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("warning", "Invalid admin ID format.");
                return null;
            }
        }
        return null;
    }

    /**
     * Parses the page number parameter for pagination.
     */
    private Integer parsePageNumber(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                int page = Integer.parseInt(pageParam);
                if (page < 1) {
                    request.getSession().setAttribute("warning", "Invalid page number, defaulting to page 1.");
                    return 1;
                }
                return page;
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("warning", "Invalid page number format, defaulting to page 1.");
                return 1;
            }
        }
        return 1;
    }

    /**
     * Calculates the total number of pages based on total logs.
     */
    private Integer calculateTotalPages(Integer totalLogs) {
        return Math.max(1, (int) Math.ceil((double) totalLogs / PAGE_SIZE));
    }

    /**
     * Calculates the total number of notifications (low stock items + pending shipments).
     */
    private Integer calculateNotifications() throws SQLException {
        int lowStockItems = productDAO.getLowStockItems();
        int pendingShipments = orderDAO.getPendingShipments();
        return lowStockItems + pendingShipments;
    }
}