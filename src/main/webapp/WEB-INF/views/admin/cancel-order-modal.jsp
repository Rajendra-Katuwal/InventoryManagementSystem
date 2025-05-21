<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="cancel-order-modal" class="modal">
	<div class="modal-content">
		<span class="modal-close" onclick="closeModal('cancel-order-modal')">
			<i class="fas fa-times"></i>
		</span>
		<h2>Cancel Order</h2>
		<p>Are you sure you want to cancel Order ID: ${order.orderId}?</p>
		<form action="${pageContext.request.contextPath}/admin/cancelOrder"
			method="POST">
			<input type="hidden" name="orderId" value="${order.orderId}">
			<input type="hidden" name="page" value="${currentPage}"> <input
				type="hidden" name="search" value="${searchQuery}">
			<div class="form-actions">
				<button type="submit" class="btn btn-delete">Confirm Cancel</button>
				<button type="button" class="btn btn-cancel"
					onclick="closeModal('cancel-order-modal')">Close</button>
			</div>
		</form>
	</div>
</div>