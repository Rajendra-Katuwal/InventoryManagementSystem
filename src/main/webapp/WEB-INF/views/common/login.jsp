<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inventoria IMS - Login</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/login.css">
</head>

<body>
	<div class="login-container">
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
			<h1 class="welcome-text">Welcome back!</h1>

			<!-- Show error message passed via request attribute -->
			<c:if test="${not empty error}">
				<div class="message error">${error}</div>
			</c:if>

			<!-- Show registration success message if registration error is false -->
			<c:if test="${param.registered == 'true'}">
				<div class="message success">Registration successful.</div>
			</c:if>

			<form action="${pageContext.request.contextPath}/login" method="post">
				<div class="form-group">
					<label for="email" class="form-label">Email</label> <input
						type="email" id="email" class="form-input" name="email"
						placeholder="pk@example.com">
				</div>

				<div class="form-group">
					<label for="password" class="form-label">Password</label> <input
						type="password" id="password" name="password" class="form-input"
						placeholder="••••••••••">
				</div>

				<div class="remember-forgot">
					<div class="checkbox-container">
						<input type="checkbox" id="rememberMe" name="rememberMe">
						<label for="rememberMe" style="margin-left: 5px;">Remember
							me?</label>
					</div>
					<a href="#" class="forgot-link">forgot password?</a>
				</div>

				<button type="submit" class="login-btn">LOGIN</button>
			</form>
		</div>
	</div>
</body>

</html>