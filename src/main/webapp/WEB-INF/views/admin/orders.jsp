<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Order Management Component -->
<div class="orders-container">
	<h1>Order Management</h1>

	<div class="orders-header">
		<form class="search-form"
			action="${pageContext.request.contextPath}/admin/orders" method="GET">
			<input type="text" class="search-input" name="search"
				placeholder="Search by Order ID or User Email..."
				value="${searchQuery}"> <input type="hidden" name="page"
				value="${currentPage}">
			<button type="submit" class="btn-search">
				<i class="fas fa-search"></i>
			</button>
		</form>
	</div>

	<!-- Notifications -->
	<c:if test="${not empty sessionScope.error}">
		<div class="notification error">${sessionScope.error}</div>
		<c:remove var="error" scope="session" />
	</c:if>
	<c:if test="${not empty sessionScope.success}">
		<div class="notification success">${sessionScope.success}</div>
		<c:remove var="success" scope="session" />
	</c:if>
	<c:if test="${not empty errorMessage}">
		<div class="notification error">${errorMessage}</div>
	</c:if>

	<!-- Pagination - Moved to top as requested -->
	<div class="pagination">
		<div class="pagination-info">Showing ${(currentPage - 1) * 10 + 1}
			to ${currentPage * 10 <= totalOrders ? currentPage * 10 : totalOrders}
			of ${totalOrders} entries</div>
		<div class="pagination-links">
			<c:if test="${currentPage > 1}">
				<a
					href="${pageContext.request.contextPath}/admin/orders?page=${currentPage - 1}&search=${searchQuery}"
					class="btn-pagination"> <i class="fas fa-chevron-left"></i>
				</a>
			</c:if>
			<c:forEach begin="1" end="${totalPages}" var="i">
				<a
					href="${pageContext.request.contextPath}/admin/orders?page=${i}&search=${searchQuery}"
					class="btn-pagination ${i == currentPage ? 'active' : ''}">${i}</a>
			</c:forEach>
			<c:if test="${currentPage < totalPages}">
				<a
					href="${pageContext.request.contextPath}/admin/orders?page=${currentPage + 1}&search=${searchQuery}"
					class="btn-pagination"> <i class="fas fa-chevron-right"></i>
				</a>
			</c:if>
		</div>
	</div>

	<!-- Table -->
	<div class="table-responsive">
		<table class="orders-table">
			<thead>
				<tr>
					<th>Order ID</th>
					<th>User Email</th>
					<th>Total Amount</th>
					<th>Status</th>
					<th>Created At</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="order" items="${orders}">
					<tr>
						<td data-label="Order ID">${order.orderId}</td>
						<td data-label="User Email">${userEmails[order.orderId]}</td>
						<td data-label="Total Amount">$${order.totalAmount}</td>
						<td data-label="Status"><c:choose>
								<c:when test="${order.orderStatus == 'pending'}">
									<span class="status-pending">${order.orderStatus}</span>
								</c:when>
								<c:when test="${order.orderStatus == 'shipped'}">
									<span class="status-in-transit">${order.orderStatus}</span>
								</c:when>
								<c:when test="${order.orderStatus == 'delivered'}">
									<span class="status-delivered">${order.orderStatus}</span>
								</c:when>
								<c:when test="${order.orderStatus == 'paid'}">
									<span class="status-delivered">${order.orderStatus}</span>
								</c:when>
								<c:otherwise>
									<span class="status-cancelled">${order.orderStatus}</span>
								</c:otherwise>
							</c:choose></td>
						<td data-label="Created At">${order.createdAt}</td>
						<td data-label="Actions" class="action-buttons"><a
							href="${pageContext.request.contextPath}/admin/viewOrder?id=${order.orderId}&page=${currentPage}&search=${searchQuery}"
							class="action-icon view-icon" title="View Order"> <i
								class="fas fa-eye"></i>
						</a> <a
							href="${pageContext.request.contextPath}/admin/editOrder?id=${order.orderId}&page=${currentPage}&search=${searchQuery}"
							class="action-icon edit-icon" title="Edit Order"> <i
								class="fas fa-edit"></i>
						</a> <c:if
								test="${order.orderStatus != 'cancelled' && order.orderStatus != 'Delivered'}">
								<a
									href="${pageContext.request.contextPath}/admin/cancelOrder?id=${order.orderId}&page=${currentPage}&search=${searchQuery}"
									class="action-icon delete-icon" title="Cancel Order"> <i
									class="fas fa-times"></i>
								</a>
							</c:if></td>
					</tr>
				</c:forEach>
				<c:if test="${empty orders}">
					<tr>
						<td colspan="6" class="no-data"><i class="fas fa-info-circle"></i>
							No orders found.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>

<!-- Include Modals -->
<jsp:include page="/WEB-INF/views/admin/view-order-modal.jsp" />
<jsp:include page="/WEB-INF/views/admin/edit-order-modal.jsp" />
<jsp:include page="/WEB-INF/views/admin/cancel-order-modal.jsp" />

<!-- Add Font Awesome if not already included -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<script>
	window.onload = function() {
		<c:if test="${not empty showModal}">
		document.getElementById('${showModal}').style.display = 'flex';
		</c:if>
	};

	function closeModal(modalId) {
		document.getElementById(modalId).style.display = 'none';
		// Redirect to preserve pagination and search state
		const page = '${currentPage}' || '1';
		const search = '${searchQuery}' || '';
		window.location.href = '${pageContext.request.contextPath}/admin/orders?page='
				+ page + '&search=' + encodeURIComponent(search);
	}
</script>