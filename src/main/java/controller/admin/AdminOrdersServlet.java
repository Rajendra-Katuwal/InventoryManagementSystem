package controller.admin;

import dao.OrderDAO;
import dao.UserDAO;
import dao.ProductDAO;
import model.Order;
import model.User;

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
 * Servlet for managing admin order operations with pagination and search.
 */
@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;
    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private ProductDAO productDAO;

    /**
     * Initializes DAO instances.
     */
    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        productDAO = new ProductDAO();
    }

    /**
     * Handles GET requests to display the list of orders.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getUserId();
        try {
            listOrders(request, response, userId);
        } catch (SQLException e) {
            session.setAttribute("error", "Unable to load orders due to a database error.");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }

    /**
     * Lists orders with pagination and search functionality.
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException, SQLException {
        // Parse pagination parameter
        String pageParam = request.getParameter("page");
        int page = 1;
        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("warning", "Invalid page number, defaulting to page 1.");
            page = 1;
        }

        // Handle search query
        String searchQuery = request.getParameter("search");
        List<Order> orders;
        int totalOrders;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            orders = orderDAO.searchOrders(searchQuery, page, PAGE_SIZE);
            totalOrders = orderDAO.getTotalSearchOrders(searchQuery);
        } else {
            orders = orderDAO.getOrders(page, PAGE_SIZE);
            totalOrders = orderDAO.getTotalOrders();
        }

        // Fetch user emails for orders
        Map<Integer, String> userEmails = new HashMap<>();
        for (Order order : orders) {
            try {
                User user = userDAO.findById(order.getUserId());
                userEmails.put(order.getOrderId(), user != null && user.getEmail() != null ? user.getEmail() : "Unknown");
            } catch (SQLException e) {
                userEmails.put(order.getOrderId(), "Unknown");
                request.getSession().setAttribute("warning", "Could not load email for some orders.");
            }
        }

        // Calculate pagination
        int totalPages = Math.max(1, (int) Math.ceil((double) totalOrders / PAGE_SIZE));

        // Fetch notification metrics
        int lowStockItems = productDAO.getLowStockItems();
        int pendingShipments = orderDAO.getPendingShipments();
        int notifications = lowStockItems + pendingShipments;

        // Set request attributes
        request.setAttribute("orders", orders);
        request.setAttribute("userEmails", userEmails);
        request.setAttribute("notifications", notifications);
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
            request.getSession().setAttribute("error", "Failed to render the orders page.");
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }

}