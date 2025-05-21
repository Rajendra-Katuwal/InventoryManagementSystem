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

@WebServlet("/admin/addProduct")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB threshold
		maxFileSize = 1024 * 1024 * 10, // 10MB max file size
		maxRequestSize = 1024 * 1024 * 50) // 50MB max request size
public class AddProductServlet extends HttpServlet {
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
			// Fetch categories for dropdown
			List<String> categories = categoryDAO.getAllCategoryNames();
			request.setAttribute("categories", categories);

			// Show the add product modal
			request.setAttribute("showModal", "add-product-modal");
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
			// Handle form submission for adding a product
			String sku = request.getParameter("sku");
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			double price = Double.parseDouble(request.getParameter("price"));
			int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
			String categoryName = request.getParameter("categoryName");
			Part filePart = request.getPart("imageFile"); // Get the file part
			
			// Get user ID from session for audit logging
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			
			// Validate SKU uniqueness
			if (productDAO.findBySku(sku) != null) {
				request.setAttribute("errorMessage", "SKU already exists. Please use a unique SKU.");
				List<String> categories = categoryDAO.getAllCategoryNames();
				request.setAttribute("categories", categories);
				request.setAttribute("showModal", "add-product-modal");
				request.setAttribute("pageTitle", "Inventory Management");
				request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
				request.setAttribute("cssFile", "admin-inventory");
				request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
				return;
			}

			// Convert category name to categoryId
			int categoryId = categoryDAO.getCategoryIdByName(categoryName);

			// Handle file upload
			String imagePath = "default-image.jpg"; // Default image if none uploaded
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
					request.setAttribute("errorMessage",
							"Invalid file type. Only JPG, JPEG, PNG, and GIF are allowed.");
					List<String> categories = categoryDAO.getAllCategoryNames();
					request.setAttribute("categories", categories);
					request.setAttribute("showModal", "add-product-modal");
					request.setAttribute("pageTitle", "Inventory Management");
					request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
					request.setAttribute("cssFile", "admin-inventory");
					request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request,
							response);
					return;
				}

				// Create unique file name to avoid conflicts
				String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
				String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				String filePath = uploadPath + File.separator + uniqueFileName;
				filePart.write(filePath);
				imagePath = UPLOAD_DIR + "/" + uniqueFileName;
			}

			Product product = new Product();
			product.setSku(sku);
			product.setName(name);
			product.setDescription(description);
			product.setPrice(price);
			product.setStockQuantity(stockQuantity);
			product.setCategoryId(categoryId);
			product.setImagePath(imagePath);

			productDAO.create(product);
			
			// Add audit log entry for product creation
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("CREATE_PRODUCT");
			log.setDetails("Created new product:" +
					", SKU=" + sku + 
					", Name=" + name + 
					", Price=$" + price + 
					", Stock=" + stockQuantity + 
					", Category=" + categoryName);
			auditLogDAO.create(log);
			
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		} catch (SQLException e) {
			request.getSession().setAttribute("error", "Database error: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid price or stock quantity.");
			try {
				List<String> categories = categoryDAO.getAllCategoryNames();
				request.setAttribute("categories", categories);
			} catch (SQLException ex) {
				request.getSession().setAttribute("error", "Database error fetching categories: " + ex.getMessage());
				response.sendRedirect(request.getContextPath() + "/admin/inventory");
				return;
			}
			request.setAttribute("showModal", "add-product-modal");
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