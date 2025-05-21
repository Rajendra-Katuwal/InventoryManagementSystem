<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Edit Product Modal -->
<div id="edit-product-modal" class="modal" style="display: none;">
	<div class="modal-content">
		<span class="modal-close" onclick="closeModal('edit-product-modal')">
			<i class="fas fa-times"></i>
		</span>
		<h2 class="modal-title">Edit Product</h2>
		<form action="${pageContext.request.contextPath}/admin/editProduct"
			method="POST" class="product-form" enctype="multipart/form-data">
			<input type="hidden" name="id" value="${product.productId}">
			<div class="form-grid">
				<div class="form-group">
					<label for="edit-product-sku">SKU</label> <input type="text"
						id="edit-product-sku" name="sku" value="${product.sku}" required>
				</div>
				<div class="form-group">
					<label for="edit-product-name">Product Name</label> <input
						type="text" id="edit-product-name" name="name"
						value="${product.name}" required>
				</div>
				<div class="form-group">
					<label for="edit-product-category">Category</label> <select
						id="edit-product-category" name="categoryName" required>
						<option value="">Select Category</option>
						<c:forEach var="category" items="${categories}">
							<option value="${category}"
								${product.categoryName == category ? 'selected' : ''}>${category}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label for="edit-product-stock">Stock Quantity</label> <input
						type="number" id="edit-product-stock" name="stockQuantity"
						value="${product.stockQuantity}" min="0" required>
				</div>
				<div class="form-group">
					<label for="edit-product-price">Price ($)</label> <input
						type="number" id="edit-product-price" name="price"
						value="${product.price}" min="0" step="0.01" required>
				</div>
			</div>
			<div class="form-group">
				<label for="edit-product-description">Description</label>
				<textarea id="edit-product-description" name="description" rows="3">${product.description}</textarea>
			</div>
			<div class="form-group">
				<label for="edit-product-image">Product Image</label>
				<div class="file-input-wrapper">
					<input type="file" id="edit-product-image" name="imageFile"
						accept="image/*"> <label for="edit-product-image"
						class="file-input-label"> <i
						class="fas fa-cloud-upload-alt"></i> Choose Image
					</label> <span class="file-name" id="edit-image-file-name">No file
						chosen</span>
				</div>
				<c:if test="${not empty product.imagePath}">
					<div class="current-image">
						<p>Current Image:</p>
						<img src="${pageContext.request.contextPath}/${product.imagePath}"
							alt="${product.name}" class="product-image-preview">
					</div>
				</c:if>
			</div>
			<div class="form-actions">
				<button type="button" class="btn btn-cancel"
					onclick="closeModal('edit-product-modal')">
					<i class="fas fa-times"></i> Cancel
				</button>
				<button type="submit" class="btn btn-primary">
					<i class="fas fa-save"></i> Save Changes
				</button>
			</div>
		</form>
	</div>
</div>