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
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/viewProduct")
public class ViewProductServlet extends HttpServlet {
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

			// Set category name
			product.setCategoryName(categoryDAO.getCategoryName(product.getCategoryId()));
			request.setAttribute("product", product);
			
			// Log product view
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			
			AuditLog log = new AuditLog();
			log.setUserId(user.getUserId());
			log.setAction("VIEW_PRODUCT");
			log.setDetails("Viewed product: ID=" + productId + ", SKU=" + product.getSku() + ", Name=" + product.getName());
			auditLogDAO.create(log);

			// Show the view product modal
			request.setAttribute("showModal", "view-product-modal");
			request.setAttribute("pageTitle", "Inventory Management");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
			request.setAttribute("cssFile", "admin-inventory");
			request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
		} catch (SQLException e) {
			request.getSession().setAttribute("error", "Database error: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/inventory");
		}
	}
}