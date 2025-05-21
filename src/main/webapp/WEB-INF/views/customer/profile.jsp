<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<div class="profile-container">
<h1 class="profile-title">My Profile</h1>
	<!-- Account Information -->
	<section class="profile-section" aria-labelledby="account-info">
		<h2 id="account-info">Account Information</h2>
		<div class="profile-picture">
			<c:choose>
				<c:when test="${not empty user.profilePic}">
					<img src="${pageContext.request.contextPath}${user.profilePic}"
						alt="Profile Picture" class="profile-picture-img">
				</c:when>
				<c:otherwise>
					<div class="profile-placeholder">${userInitials}</div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="profile-info">
			<div class="profile-info-item">
				<label>User ID:</label> <span>${user.userId}</span>
			</div>
			<div class="profile-info-item">
				<label>Name:</label> <span><c:out
						value="${user.name != null ? user.name : 'Not set'}" /></span>
			</div>
			<div class="profile-info-item">
				<label>Email:</label> <span><c:out
						value="${user.email != null ? user.email : 'Not set'}" /></span>
			</div>
			<div class="profile-info-item">
				<label>Contact Phone:</label> <span><c:out
						value="${user.contactPhone != null ? user.contactPhone : 'Not set'}" /></span>
			</div>
			<div class="profile-info-item">
				<label>Role:</label> <span><c:out
						value="${user.role != null ? user.role : 'Customer'}" /></span>
			</div>
			<div class="profile-info-item">
				<label>Account Created:</label> <span> <c:choose>
						<c:when test="${not empty user.createdAt}">
							<c:catch var="parseException">
								<fmt:parseDate value="${user.createdAt}"
									pattern="yyyy-MM-dd HH:mm:ss" var="parsedDate" />
								<fmt:formatDate value="${parsedDate}" pattern="MMM dd, yyyy" />
							</c:catch>
							<c:if test="${not empty parseException}">
                                    Not available
                                </c:if>
						</c:when>
						<c:otherwise>Not available</c:otherwise>
					</c:choose>
				</span>
			</div>
		</div>
	</section>

	<!-- Update Profile Form -->
	<section class="profile-section" aria-labelledby="update-profile">
		<h2 id="update-profile">Update Profile</h2>
		<form action="${pageContext.request.contextPath}/customer/profile"
			method="post" id="update-profile-form" enctype="multipart/form-data">
			<input type="hidden" name="action" value="updateProfile">
			<div class="form-group">
				<label for="email">Email</label> <input type="email" id="email"
					name="email" value="${user.email}" required aria-required="true"
					pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}">
			</div>
			<div class="form-group">
				<label for="contactPhone">Contact Phone</label> <input type="tel"
					id="contactPhone" name="contactPhone" value="${user.contactPhone}"
					required aria-required="true" pattern="[0-9]{10,15}">
			</div>
			<div class="form-group">
				<label for="profilePicture">Profile Picture</label> <input
					type="file" id="profilePicture" name="profilePicture"
					accept="image/jpeg,image/png,image/gif"
					aria-describedby="file-help"> <small id="file-help"
					class="form-help">JPG, PNG, or GIF (max 2MB)</small>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Update
					Profile</button>
			</div>
		</form>
	</section>

	<!-- Change Password Form -->
	<section class="profile-section" aria-labelledby="change-password">
		<h2 id="change-password">Change Password</h2>
		<form action="${pageContext.request.contextPath}/customer/profile"
			method="post" id="change-password-form">
			<input type="hidden" name="action" value="changePassword">
			<div class="form-group">
				<label for="currentPassword">Current Password</label> <input
					type="password" id="currentPassword" name="currentPassword"
					required aria-required="true">
			</div>
			<div class="form-group">
				<label for="newPassword">New Password</label> <input type="password"
					id="newPassword" name="newPassword" required aria-required="true"
					minlength="8">
			</div>
			<div class="form-group">
				<label for="confirmPassword">Confirm New Password</label> <input
					type="password" id="confirmPassword" name="confirmPassword"
					required aria-required="true">
			</div>
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Change
					Password</button>
			</div>
		</form>
	</section>
</div>

<script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>