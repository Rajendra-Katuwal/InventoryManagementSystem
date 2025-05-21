<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="view-order-modal" class="modal">
	<div class="modal-content">
		<span class="modal-close" onclick="closeModal('view-order-modal')">×</span>
		<h2>Order Details</h2>
		<div class="order-details">
			<div class="order-detail-item">
				<span class="detail-label">Order ID:</span> <span
					class="detail-value">${order.orderId}</span>
			</div>
			<div class="order-detail-item">
				<span class="detail-label">User Email:</span> <span
					class="detail-value">${userEmail}</span>
			</div>
			<div class="order-detail-item">
				<span class="detail-label">Total Amount:</span> <span
					class="detail-value">$${order.totalAmount}</span>
			</div>
			<div class="order-detail-item">
				<span class="detail-label">Status:</span> <span class="detail-value">${order.orderStatus}</span>
			</div>
			<div class="order-detail-item">
				<span class="detail-label">Created At:</span> <span
					class="detail-value">${order.createdAt}</span>
			</div>
			<div class="order-detail-item">
				<span class="detail-label">Shipping Address:</span> <span
					class="detail-value">${order.shippingAddress}</span>
			</div>
			<div class="order-detail-item">
				<span class="detail-label">Order Items:</span>
				<div class="detail-value">
					<table class="order-items-table">
						<thead>
							<tr>
								<th>Product</th>
								<th>Quantity</th>
								<th>Unit Price</th>
								<th>Total</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${orderItems}">
								<tr>
									<td>${productNames[item.orderItemId]}</td>
									<td>${item.quantity}</td>
									<td>$${item.unitPrice}</td>
									<td>$${item.quantity * item.unitPrice}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<button class="btn btn-cancel"
				onclick="closeModal('view-order-modal')">Close</button>
		</div>
	</div>
</div>