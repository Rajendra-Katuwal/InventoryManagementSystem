<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Contact Section -->
<section class="contact-section">
	<div class="contact-container">
		<h1>Contact Us</h1>
		<p>We're here to help! Fill out the form below, and we'll get back
			to you soon.</p>

		<!-- Contact Form -->
		<form action="${pageContext.request.contextPath}/contact"
			method="post" id="contactForm">
			<input type="hidden" name="redirectUrl"
				value="${pageContext.request.contextPath}/contact">
			<div class="form-group">
				<label for="name">Name</label> <input type="text" id="name"
					name="name"
					value="${sessionScope.user != null ? sessionScope.user.name : ''}"
					required>
			</div>
			<div class="form-group">
				<label for="email">Email</label> <input type="email" id="email"
					name="email"
					value="${sessionScope.user != null ? sessionScope.user.email : ''}"
					required>
			</div>
			<div class="form-group">
				<label for="subject">Subject</label> <input type="text" id="subject"
					name="subject" required>
			</div>
			<div class="form-group">
				<label for="message">Message</label>
				<textarea id="message" name="message" rows="6" required></textarea>
			</div>
			<button type="submit" class="btn btn-primary">Send Message</button>
		</form>
	</div>
</section>


<!-- JavaScript for Form Validation -->
<script>
	document.addEventListener('DOMContentLoaded', function() {
		const form = document.getElementById('contactForm');
		form.addEventListener('submit', function(event) {
			const name = document.getElementById('name').value.trim();
			const email = document.getElementById('email').value.trim();
			const subject = document.getElementById('subject').value.trim();
			const message = document.getElementById('message').value.trim();
			const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

			if (!name) {
				event.preventDefault();
				alert('Name is required.');
				return false;
			}
			if (!email || !emailRegex.test(email)) {
				event.preventDefault();
				alert('Please enter a valid email address.');
				return false;
			}
			if (!subject) {
				event.preventDefault();
				alert('Subject is required.');
				return false;
			}
			if (!message) {
				event.preventDefault();
				alert('Message is required.');
				return false;
			}
			return confirm('Are you sure you want to send this message?');
		});
	});
</script>