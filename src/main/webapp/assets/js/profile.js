// Update Profile Form
document.getElementById('update-profile-form').addEventListener('submit', function(event) {
	const email = document.getElementById('email').value.trim();
	const contactPhone = document.getElementById('contactPhone').value.trim();
	const profilePicture = document.getElementById('profilePicture').files[0];

	if (!email) {
		event.preventDefault();
		alert('Email is required.');
		return false;
	}
	if (!contactPhone) {
		event.preventDefault();
		alert('Contact phone is required.');
		return false;
	}
	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	if (!emailRegex.test(email)) {
		event.preventDefault();
		alert('Please enter a valid email address.');
		return false;
	}
	if (profilePicture) {
		const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
		if (!allowedTypes.includes(profilePicture.type)) {
			event.preventDefault();
			alert('Only JPG, PNG, or GIF files are allowed.');
			return false;
		}
		if (profilePicture.size > 2 * 1024 * 1024) {
			event.preventDefault();
			alert('Profile picture must be less than 2MB.');
			return false;
		}
	}
	return confirm('Are you sure you want to update your profile?');
});

// Change Password Form
document.getElementById('change-password-form').addEventListener('submit', function(event) {
	const currentPassword = document.getElementById('currentPassword').value.trim();
	const newPassword = document.getElementById('newPassword').value.trim();
	const confirmPassword = document.getElementById('confirmPassword').value.trim();

	if (!currentPassword) {
		event.preventDefault();
		alert('Current password is required.');
		return false;
	}
	if (!newPassword) {
		event.preventDefault();
		alert('New password is required.');
		return false;
	}
	if (newPassword !== confirmPassword) {
		event.preventDefault();
		alert('New password and confirmation do not match.');
		return false;
	}
	if (newPassword.length < 8) {
		event.preventDefault();
		alert('New password must be at least 8 characters long.');
		return false;
	}
	return confirm('Are you sure you want to change your password?');
});