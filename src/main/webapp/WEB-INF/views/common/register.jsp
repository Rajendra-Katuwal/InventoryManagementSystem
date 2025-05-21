<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="signup-container">
	<div class="left-panel">
		<div class="logo-container">
			<div class="logo">I</div>
			<div>
				<div class="brand-name">Inventoria</div>
				<div class="tagline">Your Ideal Partner</div>
			</div>
		</div>

		<div class="feature">
			<div class="feature-icon">📋</div>
			<div class="feature-text">
				<div class="feature-title">Stay Organized</div>
				<div class="feature-description">Keep Everything Under
					Control.</div>
			</div>
		</div>

		<div class="feature">
			<div class="feature-icon">📊</div>
			<div class="feature-text">
				<div class="feature-title">Get All Your Stats</div>
				<div class="feature-description">And All Reports with One
					Click.</div>
			</div>
		</div>

		<div class="feature">
			<div class="feature-icon">📱</div>
			<div class="feature-text">
				<div class="feature-title">Keep An Eye</div>
				<div class="feature-description">And Access Wherever You Are.</div>
			</div>
		</div>

		<div class="feature">
			<div class="feature-icon">🔒</div>
			<div class="feature-text">
				<div class="feature-title">Everything Secured</div>
				<div class="feature-description">Security And Safety First.</div>
			</div>
		</div>
	</div>

	<div class="right-panel">
		<h1 class="welcome-text">Create Account</h1>
		<p class="subtitle">Start managing your inventory effectively</p>

		<%-- Error message (from request attribute) --%>
		<c:if test="${not empty error}">
			<div class="error-message">${error}</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/register"
			method="post">
			<!-- First and Last Name -->
			<div class="form-row">
				<div class="form-group">
					<label for="firstname" class="form-label">First Name</label> <input
						type="text" id="firstname" name="firstname" class="form-input"
						required>
				</div>

				<div class="form-group">
					<label for="lastname" class="form-label">Last Name</label> <input
						type="text" id="lastname" name="lastname" class="form-input"
						required>
				</div>
			</div>

			<!-- Email -->
			<div class="form-group" style="margin-bottom: 20px;">
				<label for="email" class="form-label">Email Address</label> <input
					type="email" id="email" name="email" class="form-input" required>
			</div>

			<!-- Password -->
			<div class="form-row">
				<div class="form-group">
					<label for="password" class="form-label">Password</label> <input
						type="password" id="password" name="password" class="form-input"
						required>
				</div>

				<div class="form-group">
					<label for="confirm-password" class="form-label">Confirm
						Password</label> <input type="password" id="confirm-password"
						name="confirm-password" class="form-input" required>
				</div>
			</div>

			<!-- Phone Number -->
			<div class="form-group" style="margin-bottom: 20px;">
				<label for="phone" class="form-label">Phone Number</label> <input
					type="tel" id="phone" name="phone-number" class="form-input">
			</div>

			<!-- Terms and Conditions -->
			<div class="terms-container">
				<input type="checkbox" id="terms" name="terms" required> <label
					for="terms">I agree to Inventoria's <a href="#"
					class="terms-link">Terms of Service</a> and <a href="#"
					class="terms-link">Privacy Policy</a></label>
			</div>

			<button type="submit" class="signup-btn">CREATE ACCOUNT</button>

			<div class="login-link">
				Already have an account? <a href="login.html">Log in</a>
			</div>
		</form>
	</div>
</div>

