package dao;

import model.Cart;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CartDAO {

    /**
     * Adds a product to the cart or updates quantity if it exists.
     */
    public void addToCart(int userId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantity = quantity + ?, updated_at = CURRENT_TIMESTAMP";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all non-deleted cart items for a user with product details.
     */
    public List<Cart> getCartItems(int userId) throws SQLException {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, p.name, p.price, p.image_path "
                + "FROM cart c JOIN products p ON c.product_id = p.product_id "
                + "WHERE c.user_id = ? AND c.is_deleted = FALSE";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cartItems.add(mapCart(rs));
                }
            }
        }
        return cartItems;
    }

    /**
     * Updates the quantity of a specific cart item.
     */
    public void updateCartItem(int cartId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartId);
            stmt.executeUpdate();
        }
    }

    /**
     * Soft-deletes a specific cart item.
     */
    public void removeFromCart(int cartId) throws SQLException {
        String sql = "UPDATE cart SET is_deleted = TRUE WHERE cart_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }

    /**
     * Soft-deletes all cart items for a user.
     */
    public void clearCart(int userId) throws SQLException {
        String sql = "UPDATE cart SET is_deleted = TRUE WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    /**
     * Maps a ResultSet row to a Cart object.
     */
    private Cart mapCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setProductId(rs.getInt("product_id"));
        cart.setQuantity(rs.getInt("quantity"));
        cart.setProductName(rs.getString("name"));
        cart.setPrice(rs.getDouble("price"));
        cart.setImagePath(rs.getString("image_path"));
        return cart;
    }
}