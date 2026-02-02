document.addEventListener('DOMContentLoaded', () => {
    const navbar = document.querySelector('.navbar');
    const brandSuffix = document.getElementById('brand-suffix');
    const logoLink = document.getElementById('logo-link');

    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
                // Turn branding suffix (DOCS) to Orange
                if (brandSuffix) {
                    brandSuffix.classList.remove('text-white');
                    brandSuffix.classList.add('text-[#ff6b00]');
                }
                // Ensure main Logo stays Orange (it is usually orange by default, but just in case)
                if (logoLink) {
                    logoLink.classList.remove('text-black');
                    logoLink.classList.add('text-[#ff6b00]');
                }
            } else {
                navbar.classList.remove('scrolled');
                // Revert to White/Original
                if (brandSuffix) {
                    brandSuffix.classList.add('text-white');
                    brandSuffix.classList.remove('text-[#ff6b00]');
                }
                if (logoLink) {
                    logoLink.classList.add('text-[#ff6b00]');
                    logoLink.classList.remove('text-black');
                }
            }
        });

        // Highlight active link
        const currentPath = window.location.pathname;
        const navLinks = document.querySelectorAll('.nav-item');
        navLinks.forEach(link => {
            if (link.getAttribute('href') === currentPath) {
                link.classList.add('active');
            }
        });
    }
});
