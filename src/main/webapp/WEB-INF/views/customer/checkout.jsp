<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="checkout-container">
	<h1 class="checkout-title">Checkout</h1>

	<div class="checkout-content">
		<div class="checkout-items">
			<h2>Order Summary</h2>
			<table class="checkout-table">
				<thead>
					<tr>
						<th>Product</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Total</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${cartItems}">
						<tr>
							<td class="checkout-item-details"><img
								src="${pageContext.request.contextPath}/${fn:escapeXml(item.imagePath)}"
								alt="${fn:escapeXml(item.productName)}"
								class="checkout-item-image"> <span>${fn:escapeXml(item.productName)}</span>
							</td>
							<td><fmt:formatNumber value="${item.price}" type="currency"
									currencySymbol="$" /></td>
							<td>${item.quantity}</td>
							<td><fmt:formatNumber value="${item.price * item.quantity}"
									type="currency" currencySymbol="$" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="checkout-total">
				<h3>
					Total: <span><fmt:formatNumber value="${totalAmount}"
							type="currency" currencySymbol="$" /></span>
				</h3>
			</div>
		</div>

		<div class="checkout-form">
			<h2>Shipping Address</h2>
			<div id="checkout-error" class="checkout-error"
				style="display: none;"></div>
			<form action="${pageContext.request.contextPath}/customer/checkout"
				method="post" id="checkout-form">
				<div class="form-group">
					<label for="shippingAddress">Address <span class="required">*</span></label>
					<textarea id="shippingAddress" name="shippingAddress" rows="4"
						maxlength="255" required placeholder="Enter your shipping address"
						aria-required="true"></textarea>
				</div>
				<div class="form-actions">
					<a href="${pageContext.request.contextPath}/customer/cart"
						class="btn btn-secondary" aria-label="Back to cart">Back to
						Cart</a>
					<button type="submit" class="btn btn-primary"
						aria-label="Place order">Place Order</button>
				</div>
			</form>
		</div>
	</div>

	<!-- Confirmation Modal -->
	<div id="checkout-confirm-modal" class="checkout-modal" role="dialog"
		aria-labelledby="confirm-modal-title">
		<div class="checkout-modal-content">
			<span class="checkout-modal-close" onclick="closeConfirmModal()"
				aria-label="Close modal">×</span>
			<h2 id="confirm-modal-title">Confirm Order</h2>
			<p>Are you sure you want to place this order?</p>
			<div class="modal-actions">
				<button class="btn btn-secondary" onclick="closeConfirmModal()"
					aria-label="Cancel">Cancel</button>
				<button class="btn btn-primary" onclick="submitOrder()"
					aria-label="Confirm order">Confirm</button>
			</div>
		</div>
	</div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/checkout.js"></script>