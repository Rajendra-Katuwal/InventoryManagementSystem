package controller.admin;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.AuditLogDAO;
import model.Product;
import model.User;
import model.AuditLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/admin/editProduct")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB threshold
		maxFileSize = 1024 * 1024 * 10, // 10MB max file size
		maxRequestSize = 1024 * 1024 * 50) // 50MB max request size
public class EditProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductDAO productDAO = new ProductDAO();
	private CategoryDAO categoryDAO = new CategoryDAO();
	private AuditLogDAO auditLogDAO = new AuditLogDAO(); // Added for audit logging
	private static final String UPLOAD_DIR = "uploads";
	private static final String[] ALLOWED_EXTENSIONS = { ".jpg", ".jpeg", ".png", ".gif" };

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int productId = Integer.parseInt(request.getParameter("id"));
			Product product = productDAO.findById(productId);
			if (product == null) {
				request.getSession().setAttribute("error", "Product not found.");
				response.sendRedirect(request.getContextPath() + "/admin/inventory");
				return;
			}

			// Fetch categories for dropdown
			List<String> categories = categoryDAO.getAllCategoryNames();
			request.setAttribute("categories", categories);
			// Set category name for the product
			product.setCategoryName(categoryDAO.getCategoryName(product.getCategoryId()));
			request.setAttribute("product", product);

			// Show the edit product modal
			request.setAttribute("showModal", "edit-product-modal");
			request.setAttribute("pageTitle", "Inventory Management");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
			request.setAttribute("cssFile", "admin-inventory");
			request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
		} catch (SQLException e) {
			request.getSession().setAttribute("error", "Database error: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int productId = Integer.parseInt(request.getParameter("id"));
			String sku = request.getParameter("sku");
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			double price = Double.parseDouble(request.getParameter("price"));
			int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
			String categoryName = request.getParameter("categoryName");
			Part filePart = request.getPart("imageFile");
			
			HttpSession session = request.getSession();
			// Get user from session
			User user = (User) session.getAttribute("user");
			
			// Validate SKU uniqueness (excluding current product)
			Product existingProduct = productDAO.findBySku(sku);
			if (existingProduct != null && existingProduct.getProductId() != productId) {
				request.setAttribute("errorMessage", "SKU already exists. Please use a unique SKU.");
				Product product = productDAO.findById(productId);
				product.setCategoryName(categoryDAO.getCategoryName(product.getCategoryId()));
				request.setAttribute("product", product);
				List<String> categories = categoryDAO.getAllCategoryNames();
				request.setAttribute("categories", categories);
				request.setAttribute("showModal", "edit-product-modal");
				request.setAttribute("pageTitle", "Inventory Management");
				request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
				request.setAttribute("cssFile", "admin-inventory");
				request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
				return;
			}

			// Convert category name to categoryId
			int categoryId = categoryDAO.getCategoryIdByName(categoryName);

			// Fetch the existing product to get the current image path
			Product existingProductData = productDAO.findById(productId);
			String imagePath = existingProductData.getImagePath();
			
			// Prepare audit log details
			StringBuilder changes = new StringBuilder();
			changes.append("Updated product: ID=").append(productId).append(", ");
			
			// Track changed fields
			if (!existingProductData.getSku().equals(sku)) {
				changes.append("SKU: ").append(existingProductData.getSku()).append(" → ").append(sku).append(", ");
			}
			if (!existingProductData.getName().equals(name)) {
				changes.append("Name: ").append(existingProductData.getName()).append(" → ").append(name).append(", ");
			}
			if (!existingProductData.getDescription().equals(description)) {
				changes.append("Description updated, ");
			}
			if (existingProductData.getPrice() != price) {
				changes.append("Price: $").append(existingProductData.getPrice()).append(" → $").append(price).append(", ");
			}
			if (existingProductData.getStockQuantity() != stockQuantity) {
				changes.append("Stock: ").append(existingProductData.getStockQuantity()).append(" → ").append(stockQuantity).append(", ");
			}
			if (existingProductData.getCategoryId() != categoryId) {
				changes.append("Category updated, ");
			}

			// Handle file upload if a new file is provided
			if (filePart != null && filePart.getSize() > 0) {
				String fileName = extractFileName(filePart);
				// Validate file extension
				boolean validExtension = false;
				for (String ext : ALLOWED_EXTENSIONS) {
					if (fileName.toLowerCase().endsWith(ext)) {
						validExtension = true;
						break;
					}
				}
				if (!validExtension) {
					request.setAttribute("errorMessage", "Invalid file type. Only JPG, JPEG, PNG, and GIF are allowed.");
					Product product = productDAO.findById(productId);
					product.setCategoryName(categoryDAO.getCategoryName(product.getCategoryId()));
					request.setAttribute("product", product);
					List<String> categories = categoryDAO.getAllCategoryNames();
					request.setAttribute("categories", categories);
					request.setAttribute("showModal", "edit-product-modal");
					request.setAttribute("pageTitle", "Inventory Management");
					request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
					request.setAttribute("cssFile", "admin-inventory");
					request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
					return;
				}

				// Delete the old image if it exists and is not the default
				if (imagePath != null && !imagePath.equals("default-image.jpg")) {
					File oldImage = new File(getServletContext().getRealPath("") + File.separator + imagePath);
					if (oldImage.exists()) {
						oldImage.delete();
					}
				}

				// Save the new image
				String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
				String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				String filePath = uploadPath + File.separator + uniqueFileName;
				filePart.write(filePath);
				imagePath = UPLOAD_DIR + "/" + uniqueFileName;
				changes.append("Image updated, ");
			}

			Product product = new Product();
			product.setProductId(productId);
			product.setSku(sku);
			product.setName(name);
			product.setDescription(description);
			product.setPrice(price);
			product.setStockQuantity(stockQuantity);
			product.setCategoryId(categoryId);
			product.setImagePath(imagePath != null ? imagePath : "default-image.jpg");

			productDAO.update(product);
			
			// Create audit log for the update
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("UPDATE_PRODUCT");
			// Remove trailing comma and space if present
			String changeDetails = changes.toString();
			if (changeDetails.endsWith(", ")) {
				changeDetails = changeDetails.substring(0, changeDetails.length() - 2);
			}
			log.setDetails(changeDetails);
			auditLogDAO.create(log);
			
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		} catch (SQLException e) {
			request.getSession().setAttribute("error", "Database error: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid price or stock quantity.");
			int productId = Integer.parseInt(request.getParameter("id"));
			Product product = new Product();
			try {
				product = productDAO.findById(productId);
				product.setCategoryName(categoryDAO.getCategoryName(product.getCategoryId()));
				List<String> categories = categoryDAO.getAllCategoryNames();
				request.setAttribute("categories", categories);
			} catch (SQLException ex) {
				request.getSession().setAttribute("error", "Database error fetching categories: " + ex.getMessage());
				response.sendRedirect(request.getContextPath() + "/admin/inventory");
				return;
			}
			request.setAttribute("product", product);
			request.setAttribute("showModal", "edit-product-modal");
			request.setAttribute("pageTitle", "Inventory Management");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
			request.setAttribute("cssFile", "admin-inventory");
			request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
		} catch (Exception e) {
			request.getSession().setAttribute("error", "Error uploading file: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		}
	}

	// Helper method to extract file name from Part
	private String extractFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		String[] items = contentDisposition.split(";");
		for (String item : items) {
			if (item.trim().startsWith("filename")) {
				return item.substring(item.indexOf("=") + 2, item.length() - 1);
			}
		}
		return "";
	}
}