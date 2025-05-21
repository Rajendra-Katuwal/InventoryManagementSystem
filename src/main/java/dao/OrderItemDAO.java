package dao;

import model.OrderItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderItemDAO {

    /**
     * Creates a new order item.
     */
    public void create(OrderItem item) throws SQLException {
        if (item == null || item.getOrderId() <= 0 || item.getProductId() <= 0 || item.getQuantity() <= 0) {
            throw new SQLException("Order item fields (order_id, product_id, quantity) must be valid");
        }
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getUnitPrice());
            stmt.executeUpdate();
        }
    }

    /**
     * Soft-deletes an order item.
     */
    public void softDelete(int orderItemId) throws SQLException {
        String sql = "UPDATE order_items SET is_deleted = TRUE WHERE order_item_id = ? AND is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all non-deleted order items for an order.
     */
    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
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