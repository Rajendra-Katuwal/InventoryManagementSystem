<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="browse-products-container">
	<!-- Header -->
	<div class="product-grid-header">
		<form class="search-form"
			action="${pageContext.request.contextPath}/customer/browse-products"
			method="get">
			<input type="text" name="searchQuery"
				placeholder="Search products..." value="${searchQuery}"> <input
				type="hidden" name="sortBy" value="${sortBy}"> <input
				type="hidden" name="categoryId" value="${categoryId}">
			<button type="submit">
				<i class="fas fa-search"></i>
			</button>
		</form>
		<div class="filter-sort">
			<form class="filter-form"
				action="${pageContext.request.contextPath}/customer/browse-products"
				method="get">
				<label for="categoryId">Category:</label> <select id="categoryId"
					name="categoryId" onchange="this.form.submit()">
					<option value="" ${categoryId == null ? 'selected' : ''}>All
						Categories</option>
					<c:forEach var="category" items="${categories}">
						<option value="${category.id}"
							${categoryId == category.id ? 'selected' : ''}>
							${category.name}</option>
					</c:forEach>
				</select> <input type="hidden" name="sortBy" value="${sortBy}"> <input
					type="hidden" name="searchQuery" value="${searchQuery}">
			</form>
			<form class="sort-form"
				action="${pageContext.request.contextPath}/customer/browse-products"
				method="get">
				<label for="sortBy">Sort by:</label> <select id="sortBy"
					name="sortBy" onchange="this.form.submit()">
					<option value="featured" ${sortBy == 'featured' ? 'selected' : ''}>Featured</option>
					<option value="price-asc"
						${sortBy == 'price-asc' ? 'selected' : ''}>Price: Low to
						High</option>
					<option value="price-desc"
						${sortBy == 'price-desc' ? 'selected' : ''}>Price: High
						to Low</option>
				</select> <input type="hidden" name="categoryId" value="${categoryId}">
				<input type="hidden" name="searchQuery" value="${searchQuery}">
			</form>
		</div>
	</div>

	<!-- Product Grid -->
	<div class="product-grid">
		<c:forEach var="product" items="${products}">
			<div class="product-card">
				<div class="product-image">
					<img src="${pageContext.request.contextPath}/${product.imagePath}"
						alt="${product.name}">
					<form
						action="${pageContext.request.contextPath}/customer/add-to-cart"
						method="post" class="cart-icon-form">
						<input type="hidden" name="productId" value="${product.productId}">
						<input type="hidden" name="quantity" value="1">
						<button type="submit" class="cart-icon-btn"
							${product.stockQuantity <= 0 ? 'disabled' : ''}>
							<i class="fas fa-cart-plus"></i>
						</button>
					</form>
				</div>
				<div class="product-info">
					<h3 class="product-name">${product.name}</h3>
					<p class="product-description">${product.description}</p>
					<p class="product-price">
						$
						<fmt:formatNumber value="${product.price}" type="number"
							minFractionDigits="2" maxFractionDigits="2" />
					</p>
					<p
						class="stock-status ${product.stockQuantity > 0 ? 'in-stock' : 'out-of-stock'}">
						<i
							class="fas fa-${product.stockQuantity > 0 ? 'check-circle' : 'times-circle'}"></i>
						${product.stockQuantity > 0 ? 'In Stock' : 'Out of Stock'}
					</p>
					<div class="product-actions">
						<a
							href="${pageContext.request.contextPath}/customer/product-details?productId=${product.productId}"
							class="view-details-btn"> <i class="fas fa-eye"></i> View
							Details
						</a>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

	<div class="showing-info">Showing ${startIndex + 1}-${endIndex}
		of ${totalProducts} products</div>
	<!-- Pagination -->
	<div class="pagination">
		<c:if test="${currentPage > 1}">
			<a
				href="?page=${currentPage - 1}&sortBy=${sortBy}&categoryId=${categoryId}&searchQuery=${searchQuery}"
				class="pagination-btn"> <i class="fas fa-chevron-left"></i>
			</a>
		</c:if>
		<c:forEach begin="1" end="${totalPages}" var="i">
			<a
				href="?page=${i}&sortBy=${sortBy}&categoryId=${categoryId}&searchQuery=${searchQuery}"
				class="pagination-btn ${i == currentPage ? 'active' : ''}">${i}</a>
		</c:forEach>
		<c:if test="${currentPage < totalPages}">
			<a
				href="?page=${currentPage + 1}&sortBy=${sortBy}&categoryId=${categoryId}&searchQuery=${searchQuery}"
				class="pagination-btn"> <i class="fas fa-chevron-right"></i>
			</a>
		</c:if>
	</div>
</div>