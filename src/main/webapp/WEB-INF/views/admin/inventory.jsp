<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="inventory-container">
	<div class="inventory-header">
		<h1>Inventory Management</h1>
		<div class="inventory-controls">
			<form class="search-form"
				action="${pageContext.request.contextPath}/admin/inventory"
				method="GET">
				<div class="search-wrapper">
					<i class="fas fa-search search-icon"></i> <input type="text"
						class="search-input" name="search"
						placeholder="Search products..." value="${searchQuery}">
				</div>
				<button type="submit" class="btn btn-search">Search</button>
			</form>
			<a href="${pageContext.request.contextPath}/admin/addProduct"
				class="btn btn-primary"> <i class="fas fa-plus"></i> Add Product
			</a>
		</div>

		<!-- Pagination moved to top -->
		<div class="pagination">
			<div class="pagination-info">Showing ${(currentPage - 1) * 10 + 1}
				to ${currentPage * 10 <= totalProducts ? currentPage * 10 : totalProducts}
				of ${totalProducts} entries</div>
			<div class="pagination-links">
				<c:if test="${currentPage > 1}">
					<a
						href="${pageContext.request.contextPath}/admin/inventory?page=${currentPage - 1}&search=${searchQuery}"
						class="btn-pagination"> <i class="fas fa-chevron-left"></i>
					</a>
				</c:if>
				<c:forEach begin="1" end="${totalPages}" var="i">
					<a
						href="${pageContext.request.contextPath}/admin/inventory?page=${i}&search=${searchQuery}"
						class="btn-pagination ${i == currentPage ? 'active' : ''}">${i}</a>
				</c:forEach>
				<c:if test="${currentPage < totalPages}">
					<a
						href="${pageContext.request.contextPath}/admin/inventory?page=${currentPage + 1}&search=${searchQuery}"
						class="btn-pagination"> <i class="fas fa-chevron-right"></i>
					</a>
				</c:if>
			</div>
		</div>
	</div>

	<c:if test="${not empty sessionScope.error}">
		<div class="notification error">
			<i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
		</div>
		<c:remove var="error" scope="session" />
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="notification error">
			<i class="fas fa-exclamation-circle"></i> ${errorMessage}
		</div>
	</c:if>

	<div class="table-container">
		<table class="inventory-table">
			<thead>
				<tr>
					<th>Product Name</th>
					<th>Category</th>
					<th>Stock</th>
					<th>Price</th>
					<th class="actions-column">Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="product" items="${products}">
					<tr>
						<td data-label="Product Name">${product.name}</td>
						<td data-label="Category">${product.categoryName}</td>
						<td data-label="Stock"><c:choose>
								<c:when test="${product.stockQuantity <= 15}">
									<span class="low-stock"> <i
										class="fas fa-exclamation-triangle"></i>
										${product.stockQuantity}
									</span>
								</c:when>
								<c:otherwise>
                                    ${product.stockQuantity}
                                </c:otherwise>
							</c:choose></td>
						<td data-label="Price">$${product.price}</td>
						<td data-label="Actions" class="action-buttons"><a
							href="${pageContext.request.contextPath}/admin/viewProduct?id=${product.productId}"
							class="action-icon view-icon" title="View Product"> <i
								class="fas fa-eye"></i>
						</a> <a
							href="${pageContext.request.contextPath}/admin/editProduct?id=${product.productId}"
							class="action-icon edit-icon" title="Edit Product"> <i
								class="fas fa-edit"></i>
						</a> <a
							href="${pageContext.request.contextPath}/admin/deleteProduct?id=${product.productId}"
							class="action-icon delete-icon" title="Delete Product"> <i
								class="fas fa-trash-alt"></i>
						</a></td>
					</tr>
				</c:forEach>
				<c:if test="${empty products}">
					<tr>
						<td colspan="5" class="no-data"><i class="fas fa-box-open"></i>
							No products found.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>

<!-- Include Modals -->
<jsp:include page="/WEB-INF/views/admin/add-product-modal.jsp" />
<jsp:include page="/WEB-INF/views/admin/edit-product-modal.jsp" />
<jsp:include page="/WEB-INF/views/admin/delete-product-modal.jsp" />
<jsp:include page="/WEB-INF/views/admin/view-product-modal.jsp" />

<script>
	// Show modals based on request attributes (set by servlets)
	window.onload = function() {
		<c:if test="${not empty showModal}">
		document.getElementById('${showModal}').style.display = 'flex';
		</c:if>
	};

	function closeModal(modalId) {
		document.getElementById(modalId).style.display = 'none';
		// Redirect to inventory page to clear modal state
		window.location.href = '${pageContext.request.contextPath}/admin/inventory';
	}
</script>