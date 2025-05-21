// Notification Modal functionality
document.addEventListener('DOMContentLoaded', () => {
	// Auto-dismiss notifications after 5 seconds
	const notifications = document.querySelectorAll('.notification-modal');

	notifications.forEach(notification => {
		setTimeout(() => {
			if (notification && notification.parentNode) {
				notification.style.opacity = '0';
				setTimeout(() => {
					if (notification && notification.parentNode) {
						notification.parentNode.removeChild(notification);
					}
				}, 300);
			}
		}, 5000);
	});
});

// Function to close notification when X is clicked
function closeNotification(id) {
	const notification = document.getElementById(id);
	if (notification) {
		notification.style.opacity = '0';
		setTimeout(() => {
			if (notification && notification.parentNode) {
				notification.parentNode.removeChild(notification);
			}
		}, 300);
	}
}
