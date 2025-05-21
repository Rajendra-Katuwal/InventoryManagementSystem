<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="edit-order-modal" class="modal">
	<div class="modal-content">
		<span class="modal-close" onclick="closeModal('edit-order-modal')">×</span>
		<h2>Edit Order</h2>
		<form action="${pageContext.request.contextPath}/admin/editOrder"
			method="POST">
			<input type="hidden" name="orderId" value="${order.orderId}">
			<input type="hidden" name="page" value="${currentPage}"> <input
				type="hidden" name="search" value="${searchQuery}">
			<div class="form-group">
				<label for="orderStatus">Order Status:</label> <select
					id="orderStatus" name="orderStatus" required>
					<option value="Pending"
						${order.orderStatus == 'Pending' ? 'selected' : ''}>Pending</option>
					<option value="In Transit"
						${order.orderStatus == 'In Transit' ? 'selected' : ''}>In
						Transit</option>
					<option value="Delivered"
						${order.orderStatus == 'Delivered' ? 'selected' : ''}>Delivered</option>
					<option value="cancelled"
						${order.orderStatus == 'cancelled' ? 'selected' : ''}>Cancelled</option>
				</select>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn btn-save">Save</button>
				<button type="button" class="btn btn-cancel"
					onclick="closeModal('edit-order-modal')">Cancel</button>
			</div>
		</form>
	</div>
</div>