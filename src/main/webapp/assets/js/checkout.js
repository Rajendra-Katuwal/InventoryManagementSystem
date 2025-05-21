document.addEventListener('DOMContentLoaded', function() {
	const form = document.getElementById('checkout-form');
	const shippingAddress = document.getElementById('shippingAddress');
	const errorDiv = document.getElementById('checkout-error');
	let formToSubmit = false;

	form.addEventListener('submit', function(event) {
		event.preventDefault();
		const address = shippingAddress.value.trim();

		if (!address) {
			showError('Please enter a shipping address.');
			return;
		}

		if (address.length > 255) {
			showError('Shipping address cannot exceed 255 characters.');
			return;
		}

		if (!formToSubmit) {
			openConfirmModal();
		} else {
			form.submit();
		}
	});

	function showError(message) {
		errorDiv.textContent = message;
		errorDiv.style.display = 'block';
		setTimeout(() => {
			errorDiv.style.display = 'none';
		}, 5000);
	}

	window.openConfirmModal = function() {
		const modal = document.getElementById('checkout-confirm-modal');
		if (modal) {
			document.body.style.overflow = 'hidden';
			modal.style.display = 'flex';
			requestAnimationFrame(() => {
				modal.classList.add('show');
			});
		}
	};

	window.closeConfirmModal = function() {
		const modal = document.getElementById('checkout-confirm-modal');
		if (modal) {
			modal.classList.remove('show');
			setTimeout(() => {
				modal.style.display = 'none';
				document.body.style.overflow = '';
			}, 300);
		}
	};

	window.submitOrder = function() {
		formToSubmit = true;
		closeConfirmModal();
		form.submit();
	};

	// Modal event listeners
	const modal = document.getElementById('checkout-confirm-modal');
	modal.addEventListener('click', (e) => {
		if (e.target === modal) {
			closeConfirmModal();
		}
	});

	const modalContent = modal.querySelector('.checkout-modal-content');
	modalContent.addEventListener('click', (e) => {
		e.stopPropagation();
	});

	// Close on Escape key
	document.addEventListener('keydown', (e) => {
		if (e.key === 'Escape' && modal.classList.contains('show')) {
			closeConfirmModal();
		}
	});

	// Button hover and focus effects
	document.querySelectorAll('.btn').forEach(btn => {
		btn.addEventListener('mouseenter', () => {
			btn.style.transform = 'scale(1.05)';
			btn.style.boxShadow = '0 2px 8px var(--shadow-light)';
		});

		btn.addEventListener('mouseleave', () => {
			btn.style.transform = '';
			btn.style.boxShadow = '';
		});

		btn.addEventListener('focus', () => {
			btn.style.boxShadow = '0 0 0 3px var(--focus-ring)';
		});

		btn.addEventListener('blur', () => {
			btn.style.boxShadow = '';
		});
	});
});