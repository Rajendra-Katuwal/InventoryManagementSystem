package controller.customer;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet for browsing products with pagination, sorting, search, and category
 * filtering.
 */
@WebServlet("/customer/browse-products")
public class BrowseProductsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductDAO productDAO;

	@Override
	public void init() throws ServletException {
		productDAO = new ProductDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			// Parse parameters
			int page = parsePageNumber(request.getParameter("page"));
			final int pageSize = 12;
			String sortBy = sanitizeSortBy(request.getParameter("sortBy"));
			String searchQuery = request.getParameter("searchQuery") != null
					? request.getParameter("searchQuery").trim()
					: null;
			Integer categoryId = parseCategoryId(request.getParameter("categoryId"));

			// Fetch product count
			int totalProducts;
			try {
				totalProducts = productDAO.getTotalProductsCount(categoryId, searchQuery);
			} catch (SQLException e) {
				session.setAttribute("error", "Database error loading products.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			// Calculate pagination
			int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
			page = Math.max(1, Math.min(page, totalPages));
			int startIndex = (page - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageSize, totalProducts);

			// Fetch products
			List<Product> products;
			try {
				products = productDAO.getProductsByPage(startIndex, pageSize, sortBy, categoryId, searchQuery);
			} catch (SQLException e) {
				session.setAttribute("error", "Database error loading products.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}


			// Set JSP attributes
			request.setAttribute("products", products);
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("totalProducts", totalProducts);
			request.setAttribute("startIndex", startIndex);
			request.setAttribute("endIndex", endIndex);
			request.setAttribute("sortBy", sortBy);
			request.setAttribute("searchQuery", searchQuery);
			request.setAttribute("categoryId", categoryId);

			// Forward to JSP
			request.setAttribute("pageTitle", "Browse Products - Inventoria");
			request.setAttribute("contentPage", "/WEB-INF/views/customer/browse-products.jsp");
			request.setAttribute("cssFile", "browse-products");
			request.getRequestDispatcher("/WEB-INF/views/templates/user-template.jsp").forward(request, response);

		} catch (Exception e) {
			session.setAttribute("error", "Unexpected error loading products.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private int parsePageNumber(String pageStr) {
		try {
			return pageStr != null ? Integer.parseInt(pageStr) : 1;
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	private Integer parseCategoryId(String categoryIdStr) {
		try {
			return categoryIdStr != null && !categoryIdStr.isEmpty() ? Integer.parseInt(categoryIdStr) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private String sanitizeSortBy(String sortBy) {
		List<String> validSortOptions = Arrays.asList("featured", "price-asc", "price-desc");
		return sortBy != null && validSortOptions.contains(sortBy.toLowerCase()) ? sortBy.toLowerCase() : "featured";
	}
}