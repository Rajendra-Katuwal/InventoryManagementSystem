package dao;

import model.Category;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

	/**
	 * Counts all non-deleted products.
	 */
	public int getTotalProducts() throws SQLException {
		String sql = "SELECT COUNT(*) FROM products WHERE is_deleted = FALSE";
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
	 * Counts products with stock quantity ≤ 15.
	 */
	public int getLowStockItems() throws SQLException {
		String sql = "SELECT COUNT(*) FROM products WHERE stock_quantity <= 15 AND is_deleted = FALSE";
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
	 * Retrieves up to 5 low-stock products (stock ≤ 15).
	 */
	public List<Product> getLowStockProducts() throws SQLException {
		String sql = "SELECT p.*, c.name AS category_name FROM products p "
				+ "JOIN categories c ON p.category_id = c.category_id "
				+ "WHERE p.stock_quantity <= 15 AND p.is_deleted = FALSE " + "ORDER BY p.stock_quantity ASC LIMIT 5";
		List<Product> products = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				products.add(mapProduct(rs));
			}
		}
		return products;
	}

	/**
	 * Creates a new product.
	 */
	public void create(Product product) throws SQLException {
		if (product == null || product.getSku() == null || product.getName() == null || product.getPrice() < 0
				|| product.getStockQuantity() < 0 || product.getCategoryId() <= 0) {
			throw new SQLException("Invalid product data: SKU, name, price, stock, and category ID must be valid");
		}
		String sql = "INSERT INTO products (sku, name, description, price, stock_quantity, category_id, image_path) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, product.getSku());
			stmt.setString(2, product.getName());
			stmt.setString(3, product.getDescription());
			stmt.setDouble(4, product.getPrice());
			stmt.setInt(5, product.getStockQuantity());
			stmt.setInt(6, product.getCategoryId());
			stmt.setString(7, product.getImagePath());
			stmt.executeUpdate();
		}
	}

	/**
	 * Updates an existing product.
	 */
	public void update(Product product) throws SQLException {
		if (product == null || product.getProductId() <= 0 || product.getSku() == null || product.getName() == null
				|| product.getPrice() < 0 || product.getStockQuantity() < 0 || product.getCategoryId() <= 0) {
			throw new SQLException("Invalid product data: ID, SKU, name, price, stock, and category ID must be valid");
		}
		String sql = "UPDATE products SET sku = ?, name = ?, description = ?, price = ?, stock_quantity = ?, "
				+ "category_id = ?, image_path = ? WHERE product_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, product.getSku());
			stmt.setString(2, product.getName());
			stmt.setString(3, product.getDescription());
			stmt.setDouble(4, product.getPrice());
			stmt.setInt(5, product.getStockQuantity());
			stmt.setInt(6, product.getCategoryId());
			stmt.setString(7, product.getImagePath());
			stmt.setInt(8, product.getProductId());
			stmt.executeUpdate();
		}
	}

	/**
	 * Soft-deletes a product.
	 */
	public void softDelete(int productId) throws SQLException {
		String sql = "UPDATE products SET is_deleted = TRUE WHERE product_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Retrieves a product by its ID, including category name, or null if not found.
	 */
	public Product findById(int productId) throws SQLException {
		String sql = "SELECT p.*, c.name AS category_name FROM products p "
				+ "JOIN categories c ON p.category_id = c.category_id "
				+ "WHERE p.product_id = ? AND p.is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapProduct(rs);
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves a product by its SKU, or null if not found.
	 */
	public Product findBySku(String sku) throws SQLException {
		if (sku == null || sku.trim().isEmpty()) {
			throw new SQLException("SKU cannot be null or empty");
		}
		String sql = "SELECT p.*, c.name AS category_name FROM products p "
				+ "JOIN categories c ON p.category_id = c.category_id " + "WHERE p.sku = ? AND p.is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, sku);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapProduct(rs);
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves all non-deleted products.
	 */
	public List<Product> getAllProducts() throws SQLException {
		String sql = "SELECT p.*, c.name AS category_name FROM products p "
				+ "JOIN categories c ON p.category_id = c.category_id " + "WHERE p.is_deleted = FALSE";
		List<Product> products = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				products.add(mapProduct(rs));
			}
		}
		return products;
	}

	/**
	 * Retrieves products by category ID.
	 */
	public List<Product> findByCategory(int categoryId) throws SQLException {
		String sql = "SELECT p.*, c.name AS category_name FROM products p "
				+ "JOIN categories c ON p.category_id = c.category_id "
				+ "WHERE p.category_id = ? AND p.is_deleted = FALSE";
		List<Product> products = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, categoryId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					products.add(mapProduct(rs));
				}
			}
		}
		return products;
	}

	/**
	 * Retrieves paginated products with optional category, search, and sorting.
	 */
	public List<Product> getProductsByPage(int startIndex, int pageSize, String sortBy, Integer categoryId,
			String searchQuery) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT p.*, c.name AS category_name FROM products p "
				+ "JOIN categories c ON p.category_id = c.category_id " + "WHERE p.is_deleted = FALSE");
		List<Object> params = new ArrayList<>();

		if (categoryId != null && categoryId > 0) {
			sql.append(" AND p.category_id = ?");
			params.add(categoryId);
		}
		if (searchQuery != null && !searchQuery.trim().isEmpty()) {
			sql.append(" AND (p.name ILIKE ? OR p.sku ILIKE ?)");
			String searchPattern = "%" + searchQuery.trim() + "%";
			params.add(searchPattern);
			params.add(searchPattern);
		}

		switch (sortBy != null ? sortBy : "") {
		case "price-asc":
			sql.append(" ORDER BY p.price ASC");
			break;
		case "price-desc":
			sql.append(" ORDER BY p.price DESC");
			break;
		default:
			sql.append(" ORDER BY p.created_at DESC");
		}
		sql.append(" LIMIT ? OFFSET ?");
		params.add(pageSize);
		params.add(startIndex);

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			try (ResultSet rs = stmt.executeQuery()) {
				List<Product> products = new ArrayList<>();
				while (rs.next()) {
					products.add(mapProduct(rs));
				}
				return products;
			}
		}
	}

	/**
	 * Counts products with optional category and search filters.
	 */
	public int getTotalProductsCount(Integer categoryId, String searchQuery) throws SQLException {
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products WHERE is_deleted = FALSE");
		List<Object> params = new ArrayList<>();

		if (categoryId != null && categoryId > 0) {
			sql.append(" AND category_id = ?");
			params.add(categoryId);
		}
		if (searchQuery != null && !searchQuery.trim().isEmpty()) {
			sql.append(" AND (name ILIKE ? OR sku ILIKE ?)");
			String searchPattern = "%" + searchQuery.trim() + "%";
			params.add(searchPattern);
			params.add(searchPattern);
		}

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	/**
	 * Updates a product's stock quantity.
	 */
	public void updateStock(int productId, int newStock) throws SQLException {
		if (newStock < 0) {
			throw new SQLException("Stock quantity cannot be negative");
		}
		String sql = "UPDATE products SET stock_quantity = ? WHERE product_id = ? AND is_deleted = FALSE";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, newStock);
			stmt.setInt(2, productId);
			stmt.executeUpdate();
		}
	}

	/**
	 * Retrieves all non-deleted categories.
	 */
	public List<Category> getAllCategories() throws SQLException {
		String sql = "SELECT category_id, name FROM categories WHERE is_deleted = FALSE ORDER BY name";
		List<Category> categories = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				categories.add(mapCategory(rs));
			}
		}
		return categories;
	}

	/**
	 * Maps a ResultSet row to a Product object.
	 */
	private Product mapProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setProductId(rs.getInt("product_id"));
		product.setSku(rs.getString("sku"));
		product.setName(rs.getString("name"));
		product.setDescription(rs.getString("description"));
		product.setPrice(rs.getDouble("price"));
		product.setStockQuantity(rs.getInt("stock_quantity"));
		product.setCategoryId(rs.getInt("category_id"));
		product.setImagePath(rs.getString("image_path"));
		product.setCreatedAt(rs.getTimestamp("created_at").toString());
		product.setDeleted(rs.getBoolean("is_deleted"));
		product.setCategoryName(rs.getString("category_name"));
		return product;
	}

	/**
	 * Maps a ResultSet row to a Category object.
	 */
	private Category mapCategory(ResultSet rs) throws SQLException {
		Category category = new Category();
		category.setCategoryId(rs.getInt("category_id"));
		category.setName(rs.getString("name"));
		return category;
	}
}