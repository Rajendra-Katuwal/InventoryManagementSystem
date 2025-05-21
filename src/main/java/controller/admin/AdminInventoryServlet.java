package controller.admin;

import dao.ProductDAO;
import dao.CategoryDAO;
import dao.OrderDAO;
import model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/inventory")
public class AdminInventoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductDAO productDAO;
	private CategoryDAO categoryDAO;
	private static final int PAGE_SIZE = 10; // Number of products per page

	@Override
	public void init() throws ServletException {
		productDAO = new ProductDAO();
		categoryDAO = new CategoryDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get search query, category filter and page number
			String searchQuery = request.getParameter("search");
			String sortBy = request.getParameter("sort");

			// Get category filter if exists
			Integer categoryId = null;
			try {
				String categoryParam = request.getParameter("category");
				if (categoryParam != null && !categoryParam.isEmpty()) {
					categoryId = Integer.parseInt(categoryParam);
				}
			} catch (NumberFormatException e) {
				// Invalid category ID, ignore
			}

			int page = 1; // Default to page 1
			try {
				String pageParam = request.getParameter("page");
				if (pageParam != null) {
					page = Integer.parseInt(pageParam);
					if (page < 1)
						page = 1;
				}
			} catch (NumberFormatException e) {
				page = 1; // Fallback to page 1 if invalid
			}

			// Calculate starting index for pagination
			int startIndex = (page - 1) * PAGE_SIZE;

			// Fetch products with pagination, search, and category filtering
			List<Product> products = productDAO.getProductsByPage(startIndex, PAGE_SIZE, sortBy, categoryId,
					searchQuery);
			int totalProducts = productDAO.getTotalProductsCount(categoryId, searchQuery);

			// Calculate total pages
			int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);
			if (totalPages < 1)
				totalPages = 1; // Ensure at least 1 page

			// Calculate notifications
			int lowStockItems = productDAO.getLowStockItems();
			int pendingShipments = new OrderDAO().getPendingShipments();
			int notifications = lowStockItems + pendingShipments;

			// Get all categories for filter dropdown
			request.setAttribute("categories", productDAO.getAllCategories());

			// Set attributes
			request.setAttribute("products", products);
			request.setAttribute("notifications", notifications);
			request.setAttribute("searchQuery", searchQuery);
			request.setAttribute("categoryId", categoryId);
			request.setAttribute("sortBy", sortBy);
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("totalProducts", totalProducts);

			// Forward to inventory page
			request.setAttribute("pageTitle", "Inventory Management");
			request.setAttribute("contentPage", "/WEB-INF/views/admin/inventory.jsp");
			request.setAttribute("cssFile", "admin-inventory");
			request.getRequestDispatcher("/WEB-INF/views/templates/admin-template.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("error",
					"An error occurred while loading the inventory page: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/dashboard");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response); // Handle POST the same as GET for search and pagination
	}
}