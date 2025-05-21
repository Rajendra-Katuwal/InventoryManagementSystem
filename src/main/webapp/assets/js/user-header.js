document.addEventListener("DOMContentLoaded", function() {
	// Highlight active page
	const navLinks = document.querySelectorAll("header nav a");
	const currentPath = window.location.pathname;

	navLinks.forEach(link => {
		const href = link.getAttribute("href");
		if (href === currentPath || (href !== "/" && currentPath.startsWith(href))) {
			link.setAttribute("aria-current", "page");
		} else {
			link.removeAttribute("aria-current");
		}
	});

	// Toggle mobile menu
	const menuBtn = document.querySelector(".mobile-menu-btn");
	const header = document.querySelector("header");

	menuBtn.addEventListener("click", () => {
		const isOpen = header.classList.toggle("mobile-menu-open");
		menuBtn.setAttribute("aria-expanded", isOpen);
		menuBtn.textContent = isOpen ? "✖" : "☰";

		// Trap focus in mobile menu when open
		if (isOpen) {
			const focusableElements = header.querySelectorAll("a, button");
			const firstFocusable = focusableElements[0];
			const lastFocusable = focusableElements[focusableElements.length - 1];

			header.addEventListener("keydown", function trapFocus(e) {
				if (e.key === "Tab") {
					if (e.shiftKey && document.activeElement === firstFocusable) {
						e.preventDefault();
						lastFocusable.focus();
					} else if (!e.shiftKey && document.activeElement === lastFocusable) {
						e.preventDefault();
						firstFocusable.focus();
					}
				}
			});
		}
	});

	// Close menu on link click
	const navItems = header.querySelectorAll("nav a, .auth-buttons a");
	navItems.forEach(item => {
		item.addEventListener("click", () => {
			if (header.classList.contains("mobile-menu-open")) {
				header.classList.remove("mobile-menu-open");
				menuBtn.setAttribute("aria-expanded", "false");
				menuBtn.textContent = "☰";
			}
		});
	});
});