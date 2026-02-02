document.addEventListener('DOMContentLoaded', () => {
    const navbarPlaceholder = document.getElementById('navbar-placeholder');
    if (navbarPlaceholder) {
        navbarPlaceholder.innerHTML = `
            <nav class="navbar">
                <div class="logo">
                    <a href="index.html">ATHARVA</a>
                </div>
                <div class="nav-links">
                    <a href="index.html" class="nav-item">
                        <span class="nav-number">01</span>
                        <span class="nav-text">Info</span>
                    </a>
                    <a href="projects.html" class="nav-item">
                        <span class="nav-number">02</span>
                        <span class="nav-text">Projects</span>
                    </a>
                    <a href="skills.html" class="nav-item">
                        <span class="nav-number">03</span>
                        <span class="nav-text">Skills</span>
                    </a>
                    <a href="contact.html" class="nav-item">
                        <span class="nav-number">04</span>
                        <span class="nav-text">Contact</span>
                    </a>
                </div>
            </nav>
        `;

        const navbar = document.querySelector('.navbar');

        // Scroll Effect
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });

        // Highlight active link
        const currentPage = window.location.pathname.split("/").pop() || 'index.html';
        const navLinks = document.querySelectorAll('.nav-item');

        navLinks.forEach(link => {
            if (link.getAttribute('href') === currentPage) {
                link.classList.add('active');
            }
        });
    }
});
