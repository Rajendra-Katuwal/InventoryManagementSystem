// Open confirmation modal
function openConfirmationModal(action, cartId, message) {
	const modal = document.getElementById('confirmation-modal');
	const messageElement = document.getElementById('confirmation-message');
	const actionInput = document.getElementById('confirmation-action');
	const cartIdInput = document.getElementById('confirmation-cartId');

	messageElement.textContent = message;
	actionInput.value = action;
	cartIdInput.value = cartId;
	modal.style.display = 'flex';
}

// Close confirmation modal
function closeConfirmationModal() {
	const modal = document.getElementById('confirmation-modal');
	modal.style.display = 'none';
}

// Handle quantity update (show modal for zero quantities)
function handleQuantityUpdate(cartId, productName) {
	const form = document.getElementById(`quantity-form-${cartId}`);
	const input = form.querySelector('.cart-quantity-input');
	const quantity = parseInt(input.value);

	if (isNaN(quantity) || quantity < 0) {
		input.value = 1; // Reset to minimum
		// Notification handled by servlet (sessionScope.error)
		form.submit();
	} else if (quantity === 0) {
		openConfirmationModal('update', cartId, `Remove ${productName} from cart by setting quantity to 0?`);
	} else {
		form.submit();
	}
}