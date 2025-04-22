<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventoria - Warehouse Inventory Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
</head>
<body>
    <!-- Header -->
    <header>
        <div class="header-container">
            <div class="logo-container">
                <div class="logo-icon">I</div>
                <div class="logo-text">Inventoria</div>
            </div>
            
            <nav>
                <ul>
                    <li><a href="#">Home</a></li>
                    <li><a href="#">Features</a></li>
                    <li><a href="#">Pricing</a></li>
                    <li><a href="#">Resources</a></li>
                    <li><a href="#">Contact</a></li>
                </ul>
            </nav>
            
            <div class="auth-buttons">
                <a href="${pageContext.request.contextPath}/login" class="btn-login">Log In</a>
                <a href="${pageContext.request.contextPath}/register" class="btn-signup">Sign Up</a>
            </div>
            
            <button class="mobile-menu-btn">☰</button>
        </div>
    </header>
    
    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-container">
            <div class="hero-sidebar">
                <div class="tagline">Your Ideal Partner for Warehouse Management</div>
                
                <div class="feature-list">
                    <div class="feature-item">
                        <div class="feature-icon">📦</div>
                        <div class="feature-text">
                            <h3>Stay Organized</h3>
                            <p>Keep Everything Under Control with our intuitive inventory tracking system.</p>
                        </div>
                    </div>
                    
                    <div class="feature-item">
                        <div class="feature-icon">📊</div>
                        <div class="feature-text">
                            <h3>Get All Your Stats</h3>
                            <p>And All Reports with One Click. Powerful analytics to optimize your operations.</p>
                        </div>
                    </div>
                    
                    <div class="feature-item">
                        <div class="feature-icon">👁️</div>
                        <div class="feature-text">
                            <h3>Keep An Eye</h3>
                            <p>And Access Wherever You Are. Mobile-friendly interface for on-the-go management.</p>
                        </div>
                    </div>
                    
                    <div class="feature-item">
                        <div class="feature-icon">🔒</div>
                        <div class="feature-text">
                            <h3>Everything Secured</h3>
                            <p>Security And Safety First. Enterprise-grade protection for your valuable data.</p>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="hero-content">
                <h1>Simplify Your Warehouse Management</h1>
                <p>Inventoria helps businesses of all sizes streamline inventory operations, reduce costs, and improve efficiency. Our powerful yet user-friendly platform provides real-time visibility into your warehouse operations.</p>
                
                <div class="cta-buttons">
                    <a href="#" class="btn-primary">Start Free Trial</a>
                    <a href="#" class="btn-secondary">Schedule Demo</a>
                </div>
                
                <div class="stats-container">
                    <div class="stat-item">
                        <div class="stat-number">98%</div>
                        <div class="stat-label">Customer Satisfaction</div>
                    </div>
                    
                    <div class="stat-item">
                        <div class="stat-number">30%</div>
                        <div class="stat-label">Cost Reduction</div>
                    </div>
                    
                    <div class="stat-item">
                        <div class="stat-number">1000+</div>
                        <div class="stat-label">Active Users</div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Features Section -->
    <section class="features-section">
        <div class="features-container">
            <h2 class="section-title">Why Choose Inventoria</h2>
            
            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-card-icon">⚡</div>
                    <h3>Fast and Reliable</h3>
                    <p>Lightning-fast performance ensures your inventory data is always up-to-date and accessible when you need it.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-card-icon">🔄</div>
                    <h3>Real-time Synchronization</h3>
                    <p>All changes are instantly synchronized across all devices, ensuring everyone has access to the latest data.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-card-icon">📱</div>
                    <h3>Mobile Access</h3>
                    <p>Manage your inventory on the go with our mobile-friendly interface, available on all devices.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-card-icon">🔍</div>
                    <h3>Advanced Search</h3>
                    <p>Find any item in your inventory within seconds with our powerful search and filtering capabilities.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-card-icon">📋</div>
                    <h3>Customizable Reports</h3>
                    <p>Create detailed reports tailored to your specific needs with our flexible reporting tools.</p>
                </div>
                
                <div class="feature-card">
                    <div class="feature-card-icon">🔄</div>
                    <h3>Integration Ready</h3>
                    <p>Seamlessly integrates with your existing business systems including ERP, accounting, and e-commerce platforms.</p>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Footer -->
    <footer>
        <div class="footer-container">
            <div class="footer-column">
                <div class="footer-logo">
                    <div class="logo-icon">I</div>
                    <div class="logo-text" style="color: white;">Inventoria</div>
                </div>
                <p class="footer-text">Simplifying inventory management for businesses worldwide. Our mission is to help you optimize your warehouse operations and boost efficiency.</p>
            </div>
            
            <div class="footer-column">
                <h3>Company</h3>
                <ul class="footer-links">
                    <li><a href="#">About Us</a></li>
                    <li><a href="#">Our Team</a></li>
                    <li><a href="#">Careers</a></li>
                    <li><a href="#">Press</a></li>
                </ul>
            </div>
            
            <div class="footer-column">
                <h3>Resources</h3>
                <ul class="footer-links">
                    <li><a href="#">Blog</a></li>
                    <li><a href="#">Case Studies</a></li>
                    <li><a href="#">Documentation</a></li>
                    <li><a href="#">Help Center</a></li>
                </ul>
            </div>
            
            <div class="footer-column">
                <h3>Contact</h3>
                <ul class="footer-links">
                    <li><a href="#">Contact Us</a></li>
                    <li><a href="#">Support</a></li>
                    <li><a href="#">Partners</a></li>
                </ul>
            </div>
        </div>
        
        <div class="copyright">
            &copy; 2025 Inventoria. All rights reserved.
        </div>
    </footer>
    
    <script>
        // Simple mobile menu toggle
        // document.querySelector('.mobile-menu-btn').addEventListener('click', function() {
        //     document.querySelector('header').classList.toggle('mobile-menu-open');
        // });
    </script>
</body>
</html>