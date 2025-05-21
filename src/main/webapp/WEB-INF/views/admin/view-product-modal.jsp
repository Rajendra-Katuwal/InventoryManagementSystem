<!-- View Product Modal (added for completeness) -->
<div id="view-product-modal" class="modal" style="display: none;">
	<div class="modal-content">
		<span class="modal-close" onclick="closeModal('view-product-modal')">
			<i class="fas fa-times"></i>
		</span>
		<h2 class="modal-title">Product Details</h2>
		<div class="product-details">
			<div class="product-detail-row">
				<div class="detail-label">SKU</div>
				<div class="detail-value">${product.sku}</div>
			</div>
			<div class="product-detail-row">
				<div class="detail-label">Name</div>
				<div class="detail-value">${product.name}</div>
			</div>
			<div class="product-detail-row">
				<div class="detail-label">Category</div>
				<div class="detail-value">${product.categoryName}</div>
			</div>
			<div class="product-detail-row">
				<div class="detail-label">Price</div>
				<div class="detail-value">$${product.price}</div>
			</div>
			<div class="product-detail-row">
				<div class="detail-label">Stock</div>
				<div class="detail-value">
					<c:choose>
						<c:when test="${product.stockQuantity <= 15}">
							<span class="low-stock"> <i
								class="fas fa-exclamation-triangle"></i>
								${product.stockQuantity}
							</span>
						</c:when>
						<c:otherwise>
                            ${product.stockQuantity}
                        </c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="product-detail-row">
				<div class="detail-label">Description</div>
				<div class="detail-value description-value">${product.description}</div>
			</div>
			<c:if test="${not empty product.imagePath}">
				<div class="product-detail-row image-row">
					<div class="detail-label">Image</div>
					<div class="detail-value">
						<img src="${pageContext.request.contextPath}/${product.imagePath}"
							alt="${product.name}" class="product-image-preview">
					</div>
				</div>
			</c:if>
		</div>
		<div class="form-actions center">
			<button type="button" class="btn btn-primary"
				onclick="closeModal('view-product-modal')">
				<i class="fas fa-check"></i> Close
			</button>
		</div>
	</div>
</div>