package dao;

import model.SalesInsights;
import model.SalesReportEntry;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for generating sales report insights and chart data.
 */
public class SalesReportDAO {

    /**
     * Retrieves paginated sales report entries.
     */
    public List<SalesReportEntry> getSalesReport(int page, int pageSize, Integer userId, Integer productId,
                                                 Integer categoryId, String status, String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT o.order_id, u.name AS customer_name, p.name AS product_name, c.name AS category_name, " +
                "oi.quantity, oi.unit_price, (oi.quantity * oi.unit_price) AS total_price, o.order_status, o.created_at " +
                "FROM orders o JOIN users u ON o.user_id = u.user_id " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        List<Object> params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);
        sql.append(" ORDER BY o.created_at DESC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                List<SalesReportEntry> entries = new ArrayList<>();
                while (rs.next()) {
                    entries.add(mapSalesReportEntry(rs));
                }
                return entries;
            }
        }
    }

    /**
     * Counts total sales report entries.
     */
    public int getTotalSalesEntries(Integer userId, Integer productId, Integer categoryId, String status,
                                    String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM orders o JOIN users u ON o.user_id = u.user_id " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        List<Object> params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Retrieves sales insights including total sales, orders, and top products/categories.
     */
    public SalesInsights getSalesInsights(Integer userId, Integer productId, Integer categoryId, String status,
                                          String startDate, String endDate) throws SQLException {
        SalesInsights insights = new SalesInsights();
        StringBuilder sql = new StringBuilder(
                "SELECT SUM(oi.quantity * oi.unit_price) AS total_sales, COUNT(DISTINCT o.order_id) AS total_orders " +
                "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        List<Object> params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    insights.setTotalSales(rs.getDouble("total_sales"));
                    insights.setTotalOrders(rs.getInt("total_orders"));
                    insights.setAverageOrderValue(
                            rs.getInt("total_orders") > 0 ? rs.getDouble("total_sales") / rs.getInt("total_orders") : 0);
                }
            }
        }

        // Top-Selling Products
        sql = new StringBuilder(
                "SELECT p.name AS product_name, SUM(oi.quantity) AS total_quantity " +
                "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);
        sql.append(" GROUP BY p.name ORDER BY total_quantity DESC LIMIT 5");

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Integer> topProducts = new HashMap<>();
                while (rs.next()) {
                    topProducts.put(rs.getString("product_name"), rs.getInt("total_quantity"));
                }
                insights.setTopProducts(topProducts);
            }
        }

        // Top Categories
        sql = new StringBuilder(
                "SELECT c.name AS category_name, SUM(oi.quantity * oi.unit_price) AS total_sales " +
                "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);
        sql.append(" GROUP BY c.name ORDER BY total_sales DESC LIMIT 5");

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Double> topCategories = new HashMap<>();
                while (rs.next()) {
                    topCategories.put(rs.getString("category_name"), rs.getDouble("total_sales"));
                }
                insights.setTopCategories(topCategories);
            }
        }

        return insights;
    }

    /**
     * Retrieves sales by category for bar chart.
     */
    public Map<String, Double> getSalesByCategory(Integer userId, Integer productId, Integer categoryId, String status,
                                                  String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT c.name AS category_name, SUM(oi.quantity * oi.unit_price) AS total_sales " +
                "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        List<Object> params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);
        sql.append(" GROUP BY c.name");

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Double> salesByCategory = new HashMap<>();
                while (rs.next()) {
                    salesByCategory.put(rs.getString("category_name"), rs.getDouble("total_sales"));
                }
                return salesByCategory;
            }
        }
    }

    /**
     * Retrieves order counts by status for pie chart.
     */
    public Map<String, Integer> getOrdersByStatus(Integer userId, Integer productId, Integer categoryId, String status,
                                                  String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT o.order_status, COUNT(*) AS order_count " +
                "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        List<Object> params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);
        sql.append(" GROUP BY o.order_status");

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Integer> ordersByStatus = new HashMap<>();
                while (rs.next()) {
                    ordersByStatus.put(rs.getString("order_status"), rs.getInt("order_count"));
                }
                return ordersByStatus;
            }
        }
    }

    /**
     * Retrieves daily sales trends for line chart.
     */
    public Map<String, Double> getSalesTrend(Integer userId, Integer productId, Integer categoryId, String status,
                                             String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT DATE(o.created_at) AS sale_date, SUM(oi.quantity * oi.unit_price) AS total_sales " +
                "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "JOIN categories c ON p.category_id = c.category_id ");
        List<Object> params = new ArrayList<>();
        appendConditions(sql, params, userId, productId, categoryId, status, startDate, endDate);
        sql.append(" GROUP BY DATE(o.created_at) ORDER BY sale_date");

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<String, Double> salesTrend = new HashMap<>();
                while (rs.next()) {
                    salesTrend.put(rs.getString("sale_date"), rs.getDouble("total_sales"));
                }
                return salesTrend;
            }
        }
    }

    /**
     * Appends SQL conditions and parameters for filtering sales data.
     */
    private void appendConditions(StringBuilder sql, List<Object> params, Integer userId, Integer productId,
                                  Integer categoryId, String status, String startDate, String endDate) {
        List<String> conditions = new ArrayList<>();
        if (userId != null) {
            conditions.add("o.user_id = ?");
            params.add(userId);
        }
        if (productId != null) {
            conditions.add("oi.product_id = ?");
            params.add(productId);
        }
        if (categoryId != null) {
            conditions.add("p.category_id = ?");
            params.add(categoryId);
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("o.order_status = ?");
            params.add(status);
        }
        if (startDate != null && !startDate.isEmpty()) {
            conditions.add("o.created_at >= ?");
            params.add(startDate + " 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            conditions.add("o.created_at <= ?");
            params.add(endDate + " 23:59:59");
        }
        if (!conditions.isEmpty()) {
            sql.append(" WHERE o.is_deleted = FALSE AND p.is_deleted = FALSE AND c.is_deleted = FALSE AND ")
               .append(String.join(" AND ", conditions));
        } else {
            sql.append(" WHERE o.is_deleted = FALSE AND p.is_deleted = FALSE AND c.is_deleted = FALSE");
        }
    }

    /**
     * Sets parameters for a PreparedStatement from a list.
     */
    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }
    }

    /**
     * Maps a ResultSet row to a SalesReportEntry object.
     */
    private SalesReportEntry mapSalesReportEntry(ResultSet rs) throws SQLException {
        SalesReportEntry entry = new SalesReportEntry();
        entry.setOrderId(rs.getInt("order_id"));
        entry.setCustomerName(rs.getString("customer_name"));
        entry.setProductName(rs.getString("product_name"));
        entry.setCategoryName(rs.getString("category_name"));
        entry.setQuantity(rs.getInt("quantity"));
        entry.setUnitPrice(rs.getDouble("unit_price"));
        entry.setTotalPrice(rs.getDouble("total_price"));
        entry.setOrderStatus(rs.getString("order_status"));
        entry.setOrderDate(rs.getTimestamp("created_at"));
        return entry;
    }
}