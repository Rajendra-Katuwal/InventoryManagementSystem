package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO {

    /**
     * Retrieves all category names in alphabetical order.
     */
    public List<String> getAllCategoryNames() throws SQLException {
        List<String> categoryNames = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name ASC";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categoryNames.add(mapCategoryName(rs));
            }
        }
        return categoryNames;
    }

    /**
     * Retrieves a category name by its ID, or null if not found.
     */
    public String getCategoryName(int categoryId) throws SQLException {
        String sql = "SELECT name FROM categories WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCategoryName(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a category ID by its name, throwing an exception if not found.
     */
    public int getCategoryIdByName(String categoryName) throws SQLException {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new SQLException("Category name cannot be null or empty");
        }
        String sql = "SELECT category_id FROM categories WHERE name = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
        }
        throw new SQLException("Category not found: " + categoryName);
    }

    /**
     * Maps a ResultSet row to a category name.
     */
    private String mapCategoryName(ResultSet rs) throws SQLException {
        return rs.getString("name");
    }
}