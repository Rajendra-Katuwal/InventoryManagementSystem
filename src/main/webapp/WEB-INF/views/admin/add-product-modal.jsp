<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Add Product Modal -->
<div id="add-product-modal" class="modal" style="display: none;">
	<div class="modal-content">
		<span class="modal-close" onclick="closeModal('add-product-modal')">
			<i class="fas fa-times"></i>
		</span>
		<h2 class="modal-title">Add New Product</h2>
		<form action="${pageContext.request.contextPath}/admin/addProduct"
			method="POST" class="product-form" enctype="multipart/form-data">
			<div class="form-grid">
				<div class="form-group">
					<label for="add-product-sku">SKU</label> <input type="text"
						id="add-product-sku" name="sku" required>
				</div>
				<div class="form-group">
					<label for="add-product-name">Product Name</label> <input
						type="text" id="add-product-name" name="name" required>
				</div>
				<div class="form-group">
					<label for="add-product-category">Category</label> <select
						id="add-product-category" name="categoryName" required>
						<option value="">Select Category</option>
						<c:forEach var="category" items="${categories}">
							<option value="${category}">${category}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label for="add-product-stock">Stock Quantity</label> <input
						type="number" id="add-product-stock" name="stockQuantity" min="0"
						required>
				</div>
				<div class="form-group">
					<label for="add-product-price">Price ($)</label> <input
						type="number" id="add-product-price" name="price" min="0"
						step="0.01" required>
				</div>
			</div>
			<div class="form-group">
				<label for="add-product-description">Description</label>
				<textarea id="add-product-description" name="description" rows="3"></textarea>
			</div>
			<div class="form-group">
				<label for="add-product-image">Product Image</label>
				<div class="file-input-wrapper">
					<input type="file" id="add-product-image" name="imageFile"
						accept="image/*"> <label for="add-product-image"
						class="file-input-label"> <i
						class="fas fa-cloud-upload-alt"></i> Choose Image
					</label> <span class="file-name" id="add-image-file-name">No file
						chosen</span>
				</div>
			</div>
			<div class="form-actions">
				<button type="button" class="btn btn-cancel"
					onclick="closeModal('add-product-modal')">
					<i class="fas fa-times"></i> Cancel
				</button>
				<button type="submit" class="btn btn-primary">
					<i class="fas fa-plus"></i> Add Product
				</button>
			</div>
		</form>
	</div>
</div>