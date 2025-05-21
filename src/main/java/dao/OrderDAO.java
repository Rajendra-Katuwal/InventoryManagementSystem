package dao;

import model.Order;
import model.OrderItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderDAO {

    /**
     * Represents a revenue entry for a time period.
     */
    public static class RevenueEntry {
        private String periodLabel;
        private double revenue;

        public String getPeriodLabel() {
            return periodLabel;
        }

        public void setPeriodLabel(String periodLabel) {
            this.periodLabel = periodLabel;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }
    }

    /**
     * Retrieves revenue data for a specified time period (weekly, monthly, yearly).
     */
    public List<RevenueEntry> getRevenueData(String period) throws SQLException {
        String sql;
        List<RevenueEntry> revenueData = new ArrayList<>();

        switch (period) {
            case "weekly":
                sql = "SELECT DATE(created_at) as period, SUM(total_amount) as revenue "
                        + "FROM orders WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY) AND is_deleted = FALSE "
                        + "GROUP BY DATE(created_at) ORDER BY DATE(created_at) ASC";
                break;
            case "monthly":
                sql = "SELECT CONCAT('Week ', WEEK(created_at, 1) - WEEK(DATE_SUB(NOW(), INTERVAL 30 DAY), 1) + 1) as period, "
                        + "SUM(total_amount) as revenue "
                        + "FROM orders WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) AND is_deleted = FALSE "
                        + "GROUP BY WEEK(created_at, 1) ORDER BY MIN(created_at) ASC";
                break;
            case "yearly":
            default:
                sql = "SELECT DATE_FORMAT(created_at, '%Y-%m') as period, SUM(total_amount) as revenue "
                        + "FROM orders WHERE created_at >= DATE_SUB(NOW(), INTERVAL 1 YEAR) AND is_deleted = FALSE "
                        + "GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY MIN(created_at) ASC";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                RevenueEntry entry = new RevenueEntry();
                entry.setPeriodLabel(rs.getString("period"));
                entry.setRevenue(rs.getDouble("revenue"));
                revenueData.add(entry);
            }
        }
        return revenueData;
    }

    /**
     * Updates the status of an order.
     */
    public void updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    /**
     * Soft-deletes an order.
     */
    public void softDelete(int orderId) throws SQLException {
        String sql = "UPDATE orders SET is_deleted = TRUE WHERE order_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves an order by its ID, or null if not found.
     */
    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a user's orders with pagination.
     */
    public List<Order> getOrderByUserId(int userId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM orders WHERE user_id = ? AND is_deleted = FALSE ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Counts a user's non-deleted orders.
     */
    public int getOrderCountByUserId(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Retrieves items for an order.
     */
    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND is_deleted = FALSE";
        List<OrderItem> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }
        return items;
    }

    /**
     * Retrieves a product name by its ID, or null if not found.
     */
    public String getProductName(int productId) throws SQLException {
        String sql = "SELECT name FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a user's email by their ID, or null if not found.
     */
    public String getUserEmail(int userId) throws SQLException {
        String sql = "SELECT email FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        }
        return null;
    }

    /**
     * Checks if an order belongs to a user.
     */
    public boolean isUserOrder(int orderId, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_id = ? AND user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves all non-deleted orders.
     */
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT * FROM orders WHERE is_deleted = FALSE";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        }
        return orders;
    }

    /**
     * Retrieves orders with pagination.
     */
    public List<Order> getOrders(int page, int pageSize) throws SQLException {
        String sql = "SELECT * FROM orders WHERE is_deleted = FALSE ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Searches orders by order ID or user email with pagination.
     */
    public List<Order> searchOrders(String searchQuery, int page, int pageSize) throws SQLException {
        String sql = "SELECT o.* FROM orders o JOIN users u ON o.user_id = u.user_id "
                + "WHERE o.is_deleted = FALSE AND (CAST(o.order_id AS CHAR) LIKE ? OR u.email LIKE ?) "
                + "ORDER BY o.created_at DESC LIMIT ? OFFSET ?";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setInt(3, pageSize);
            stmt.setInt(4, (page - 1) * pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    /**
     * Counts all non-deleted orders.
     */
    public int getTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Counts orders matching a search query by order ID or user email.
     */
    public int getTotalSearchOrders(String searchQuery) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders o JOIN users u ON o.user_id = u.user_id "
                + "WHERE o.is_deleted = FALSE AND (CAST(o.order_id AS CHAR) LIKE ? OR u.email LIKE ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Counts orders with "In Transit" status.
     */
    public int getPendingShipments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_status = 'In Transit' AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Counts a user's pending orders.
     */
    public int getPendingOrdersForUser(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND order_status = 'Pending' AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Counts a user's orders with "In Transit" status.
     */
    public int getInTransitShipmentsForUser(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE user_id = ? AND order_status = 'In Transit' AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Calculates the total amount spent by a user on non-deleted orders.
     */
    public double getTotalSpentForUser(int userId) throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE user_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    /**
     * Creates a new order and returns its ID.
     */
    public int createOrder(int userId, double totalAmount, String shippingAddress) throws SQLException {
        String sql = "INSERT INTO orders (user_id, order_status, total_amount, shipping_address) VALUES (?, 'pending', ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, shippingAddress);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("Failed to retrieve order ID.");
        }
    }

    /**
     * Adds multiple order items to an order.
     */
    public void addOrderItems(int orderId, List<OrderItem> items) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (OrderItem item : items) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getUnitPrice());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    /**
     * Maps a ResultSet row to an Order object.
     */
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setDeleted(rs.getBoolean("is_deleted"));
        return order;
    }

    /**
     * Maps a ResultSet row to an OrderItem object.
     */
    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setDeleted(rs.getBoolean("is_deleted"));
        return item;
    }
}