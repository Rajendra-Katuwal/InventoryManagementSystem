<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="container">
	<div class="product-container">
		<div class="product-gallery" role="region" aria-label="Product Image">
			<c:choose>
				<c:when test="${not empty product.imagePath}">
					<img src="${pageContext.request.contextPath}/${product.imagePath}"
						alt="${product.name}" class="main-image">
				</c:when>
				<c:otherwise>
					<img
						src="${pageContext.request.contextPath}/assets/images/no-image.jpg"
						alt="Product Image Unavailable" class="main-image">
				</c:otherwise>
			</c:choose>
		</div>

		<div class="product-info">
			<div class="product-category" aria-label="Category">
				<c:out
					value="${product.categoryName != null ? product.categoryName : 'Uncategorized'}" />
			</div>
			<h1 class="product-title">
				<c:out
					value="${product.name != null ? product.name : 'Product Name Unavailable'}" />
			</h1>
			<div class="product-sku">
				SKU:
				<c:out value="${product.sku != null ? product.sku : 'N/A'}" />
			</div>
			<div class="product-price" aria-label="Price">
				<fmt:formatNumber value="${product.price}" type="currency"
					currencySymbol="$" />
			</div>
			<p class="product-description">
				<c:out
					value="${product.description != null ? product.description : 'No description available'}" />
			</p>
			<div class="product-meta" role="region"
				aria-label="Stock Information">
				<c:choose>
					<c:when test="${product.stockQuantity > 10}">
						<div class="stock-status">
							<span class="stock-dot in-stock"></span> <span class="in-stock">In
								Stock</span>
						</div>
						<div class="product-stock">
							<c:out value="${product.stockQuantity}" />
							items available
						</div>
					</c:when>
					<c:when test="${product.stockQuantity > 0}">
						<div class="stock-status">
							<span class="stock-dot low-stock"></span> <span class="low-stock">Low
								Stock</span>
						</div>
						<div class="product-stock">
							Only
							<c:out value="${product.stockQuantity}" />
							left
						</div>
					</c:when>
					<c:otherwise>
						<div class="stock-status">
							<span class="stock-dot out-of-stock"></span> <span
								class="out-of-stock">Out of Stock</span>
						</div>
					</c:otherwise>
				</c:choose>
			</div>

			<c:if test="${product.stockQuantity > 0}">
				<form
					action="${pageContext.request.contextPath}/customer/add-to-cart"
					method="post">
					<input type="hidden" name="productId" value="${product.productId}">
					<div class="quantity-selector" role="region"
						aria-label="Quantity Selector">
						<button type="button" class="quantity-btn quantity-minus"
							aria-label="Decrease Quantity">-</button>
						<input type="text" name="quantity" class="quantity-input"
							value="1" readonly aria-label="Quantity"
							data-max="${product.stockQuantity}">
						<button type="button" class="quantity-btn quantity-plus"
							aria-label="Increase Quantity">+</button>
					</div>
					<div class="product-actions">
						<button type="submit" class="btn btn-primary"
							aria-label="Add to Cart">
							<i class="fas fa-shopping-cart"></i> Add to Cart
						</button>
					</div>
				</form>
			</c:if>

			<c:if test="${product.stockQuantity <= 0}">
				<div class="out-of-stock-message" role="alert">This product is
					currently out of stock.</div>
			</c:if>
		</div>
	</div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const minusBtn = document.querySelector('.quantity-minus');
        const plusBtn = document.querySelector('.quantity-plus');
        const qtyInput = document.querySelector('.quantity-input');

        if (minusBtn && plusBtn && qtyInput) {
            const maxStock = parseInt(qtyInput.dataset.max) || 1;

            minusBtn.addEventListener('click', () => {
                let currentValue = parseInt(qtyInput.value);
                if (currentValue > 1) {
                    qtyInput.value = currentValue - 1;
                }
            });

            plusBtn.addEventListener('click', () => {
                let currentValue = parseInt(qtyInput.value);
                if (currentValue < maxStock) {
                    qtyInput.value = currentValue + 1;
                }
            });

            // Keyboard accessibility
            qtyInput.addEventListener('keydown', (e) => {
                if (e.key === 'ArrowUp' && parseInt(qtyInput.value) < maxStock) {
                    qtyInput.value = parseInt(qtyInput.value) + 1;
                } else if (e.key === 'ArrowDown' && parseInt(qtyInput.value) > 1) {
                    qtyInput.value = parseInt(qtyInput.value) - 1;
                }
            });
        }
    });
</script>