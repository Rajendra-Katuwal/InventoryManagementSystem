package controller.admin;

import dao.ProductDAO;
import dao.CategoryDAO;
import dao.AuditLogDAO;
import model.Product;
import model.User;
import model.AuditLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/deleteProduct")
public class DeleteProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductDAO productDAO = new ProductDAO();
	private CategoryDAO categoryDAO = new CategoryDAO();
	private AuditLogDAO auditLogDAO = new AuditLogDAO(); // Added for audit logging

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

			// Set category name for display
			product.setCategoryName(categoryDAO.getCategoryName(product.getCategoryId()));
			request.setAttribute("product", product);
			request.setAttribute("showModal", "delete-product-modal");
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
			Product product = productDAO.findById(productId);
			HttpSession session = request.getSession();
			
			// Get user from session
			User user = (User) session.getAttribute("user");
			
			if (product != null) {
				// Delete the image file if it exists and is not the default
				String imagePath = product.getImagePath();
				if (imagePath != null && !imagePath.equals("default-image.jpg")) {
					File imageFile = new File(getServletContext().getRealPath("") + File.separator + imagePath);
					if (imageFile.exists()) {
						imageFile.delete();
					}
				}
				
				// Log product deletion
				AuditLog log = new AuditLog();
				log.setUserId(user.getUserId());
				log.setAction("DELETE_PRODUCT");
				log.setDetails("Deleted product: ID=" + productId + ", SKU=" + product.getSku() + ", Name=" + product.getName());
				auditLogDAO.create(log);
			}
			
			productDAO.softDelete(productId);
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		} catch (SQLException e) {
			request.getSession().setAttribute("error", "Database error: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		}
	}
}