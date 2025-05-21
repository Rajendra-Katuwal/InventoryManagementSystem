<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!-- Delete Product Modal -->
<div id="delete-product-modal" class="modal" style="display: none;">
	<div class="modal-content modal-sm">
		<span class="modal-close" onclick="closeModal('delete-product-modal')">
			<i class="fas fa-times"></i>
		</span>
		<div class="modal-icon warning">
			<i class="fas fa-exclamation-triangle"></i>
		</div>
		<h2 class="modal-title">Confirm Deletion</h2>
		<p class="modal-message">
			Are you sure you want to delete the product "<span
				id="delete-product-name">${product.name}</span>"? This action cannot
			be undone.
		</p>
		<form action="${pageContext.request.contextPath}/admin/deleteProduct"
			method="POST">
			<input type="hidden" name="id" value="${product.productId}">
			<div class="form-actions">
				<button type="button" class="btn btn-cancel"
					onclick="closeModal('delete-product-modal')">
					<i class="fas fa-times"></i> Cancel
				</button>
				<button type="submit" class="btn btn-delete">
					<i class="fas fa-trash-alt"></i> Delete
				</button>
			</div>
		</form>
	</div>
</div>
