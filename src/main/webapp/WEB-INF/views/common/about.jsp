<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="about-container" id="aboutContainer">
	<h1 class="about-title">About Inventoria</h1>

	<div class="about-content">
		<!-- Introduction Section -->
		<section class="about-section" aria-labelledby="story-heading">
			<h2 id="story-heading">Our Story</h2>
			<div class="about-card">
				<img
					src="${pageContext.request.contextPath}/assets/images/BlackLogo.png"
					alt="Inventoria Logo" class="about-image">
				<p>Founded in 2023, Inventoria is a cutting-edge inventory
					management system designed to empower businesses of all sizes. Our
					journey began with a simple goal: to simplify the complexities of
					inventory management, enabling businesses to focus on growth and
					customer satisfaction.</p>
				<p>At Inventoria, we combine innovative technology with
					user-centric design to deliver a platform that streamlines order
					processing, inventory tracking, and customer engagement. From small
					retailers to large enterprises, Inventoria is trusted worldwide to
					keep operations running smoothly.</p>
			</div>
		</section>

		<!-- Mission and Vision -->
		<section class="about-section"
			aria-labelledby="mission-vision-heading">
			<h2 id="mission-vision-heading">Mission & Vision</h2>
			<div class="about-card">
				<p>
					<strong>Mission:</strong> To provide businesses with intuitive,
					reliable, and scalable inventory management solutions that enhance
					efficiency and drive success.
				</p>
				<p>
					<strong>Vision:</strong> To be the global leader in inventory
					management, transforming how businesses manage their resources
					through technology and innovation.
				</p>
			</div>
		</section>

		<!-- Features Section -->
		<section class="about-section" aria-labelledby="features-heading">
			<h2 id="features-heading">Why Choose Inventoria?</h2>
			<div class="features-grid">
				<div class="feature-card">
					<i class="fas fa-shopping-cart feature-icon" aria-hidden="true"></i>
					<h3>Seamless Order Management</h3>
					<p>Create, track, and manage customer orders with real-time
						status updates and detailed audit logs.</p>
				</div>
				<div class="feature-card">
					<i class="fas fa-user feature-icon" aria-hidden="true"></i>
					<h3>Personalized User Profiles</h3>
					<p>Empower customers with customizable profiles to manage their
						details, orders, and preferences securely.</p>
				</div>
				<div class="feature-card">
					<i class="fas fa-box feature-icon" aria-hidden="true"></i>
					<h3>Advanced Inventory Tracking</h3>
					<p>Monitor stock levels, automate reordering, and receive
						alerts for low inventory to prevent stockouts.</p>
				</div>
				<div class="feature-card">
					<i class="fas fa-chart-line feature-icon" aria-hidden="true"></i>
					<h3>Insightful Analytics</h3>
					<p>Gain valuable insights with reports on sales, inventory
						turnover, and customer behavior.</p>
				</div>
			</div>
		</section>

		<!-- Team Section -->
		<section class="about-section" aria-labelledby="team-heading">
			<h2 id="team-heading">Meet Our Team</h2>
			<div class="team-grid">
				<div class="team-card">
					<img
						src="${pageContext.request.contextPath}/assets/images/developer1.jpeg"
						alt="Portrait of Alex Carter" class="team-image">
					<h3>Alex Carter</h3>
					<p class="team-role">Lead Developer</p>
					<p>Alex spearheaded the backend architecture of Inventoria,
						ensuring robust order and inventory management with Java and SQL.</p>
				</div>
				<div class="team-card">
					<img
						src="${pageContext.request.contextPath}/assets/images/developer2.jpeg"
						alt="Portrait of Sofia Nguyen" class="team-image">
					<h3>Sofia Nguyen</h3>
					<p class="team-role">Frontend Developer</p>
					<p>Sofia crafted the responsive, user-friendly UI using JSP,
						CSS, and JavaScript, making Inventoria visually appealing and
						intuitive.</p>
				</div>
				<div class="team-card">
					<img
						src="${pageContext.request.contextPath}/assets/images/developer3.jpeg"
						alt="Portrait of Liam Patel" class="team-image">
					<h3>Liam Patel</h3>
					<p class="team-role">Database Engineer</p>
					<p>Liam optimized the database schema and queries, enabling
						fast and secure data handling for users and orders.</p>
				</div>
				<div class="team-card">
					<img
						src="${pageContext.request.contextPath}/assets/images/developer1.jpeg"
						alt="Portrait of Emma Rodriguez" class="team-image">
					<h3>Emma Rodriguez</h3>
					<p class="team-role">Security Specialist</p>
					<p>Emma implemented BCrypt password hashing and audit logging
						to ensure Inventoria's security and compliance.</p>
				</div>
				<div class="team-card">
					<img
						src="${pageContext.request.contextPath}/assets/images/developer2.jpeg"
						alt="Portrait of Noah Kim" class="team-image">
					<h3>Noah Kim</h3>
					<p class="team-role">UI/UX Designer</p>
					<p>Noah designed the sleek, modern interface, ensuring a
						seamless experience across devices with a focus on usability.</p>
				</div>
			</div>
		</section>

		<!-- Values Section -->
		<section class="about-section" aria-labelledby="values-heading">
			<h2 id="values-heading">Our Core Values</h2>
			<div class="values-grid">
				<div class="value-card">
					<h3>Innovation</h3>
					<p>We embrace the latest technologies to deliver cutting-edge
						solutions.</p>
				</div>
				<div class="value-card">
					<h3>Reliability</h3>
					<p>Our platform is built to perform consistently, ensuring your
						business never misses a beat.</p>
				</div>
				<div class="value-card">
					<h3>Customer-Centricity</h3>
					<p>Your success is our priority, with tools designed around
						your needs.</p>
				</div>
			</div>
		</section>

		<!-- Call to Action -->
		<section class="about-section" aria-labelledby="cta-heading">
			<h2 id="cta-heading">Join the Inventoria Community</h2>
			<div class="about-card">
				<p>Ready to transform your inventory management? Sign up today
					or contact us to learn how Inventoria can elevate your business.</p>
				<a href="${pageContext.request.contextPath}/register"
					class="btn btn-primary" role="button">Get Started</a>
			</div>
		</section>
	</div>
</div>