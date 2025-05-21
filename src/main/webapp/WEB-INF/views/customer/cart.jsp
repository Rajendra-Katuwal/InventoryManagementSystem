<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="cart-container">
	<h1 class="cart-title">Your Shopping Cart</h1>

	<c:choose>
		<c:when test="${empty cartItems}">
			<div class="cart-empty">
				<p>Your cart is empty.</p>
				<a
					href="${pageContext.request.contextPath}/customer/browse-products"
					class="btn btn-primary">Browse Products</a>
			</div>
		</c:when>
		<c:otherwise>
			<div class="cart-items">
				<table class="cart-table">
					<thead>
						<tr>
							<th>Product</th>
							<th>Price</th>
							<th>Quantity</th>
							<th>Total</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${cartItems}">
							<tr>
								<td class="cart-item-details"><img
									src="${pageContext.request.contextPath}/${item.imagePath}"
									alt="${item.productName}" class="cart-item-image"> <span>${item.productName}</span>
								</td>
								<td><fmt:formatNumber value="${item.price}" type="currency"
										currencySymbol="$" /></td>
								<td>
									<form action="${pageContext.request.contextPath}/customer/cart"
										method="post" class="cart-quantity-form">
										<input type="hidden" name="action" value="update"> <input
											type="hidden" name="cartId" value="${item.cartId}"> <input
											type="number" name="quantity" value="${item.quantity}"
											min="0" onchange="this.form.submit()"
											class="cart-quantity-input">
									</form>
								</td>
								<td><fmt:formatNumber value="${item.price * item.quantity}"
										type="currency" currencySymbol="$" /></td>
								<td>
									<form action="${pageContext.request.contextPath}/customer/cart"
										method="post"
										onsubmit="return confirm('Are you sure you want to remove this item?');">
										<input type="hidden" name="action" value="remove"> <input
											type="hidden" name="cartId" value="${item.cartId}">
										<button type="submit" class="btn btn-error btn-small">Remove</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="cart-summary">
				<h3>
					Cart Total:
					<fmt:formatNumber value="${totalAmount}" type="currency"
						currencySymbol="$" />
				</h3>
				<div class="cart-actions">
					<form action="${pageContext.request.contextPath}/customer/cart"
						method="post"
						onsubmit="return confirm('Are you sure you want to clear your cart?');">
						<input type="hidden" name="action" value="clear">
						<button type="submit" class="btn btn-error">Clear Cart</button>
					</form>
					<a href="${pageContext.request.contextPath}/customer/checkout"
						class="btn btn-primary">Proceed to Checkout</a>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>