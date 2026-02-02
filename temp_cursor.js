document.addEventListener('DOMContentLoaded', () => {
    // Create cursor elements
    const cursor = document.createElement('div');
    cursor.id = 'custom-cursor';
    document.body.appendChild(cursor);

    const cursorFollower = document.createElement('div');
    cursorFollower.id = 'custom-cursor-follower';
    document.body.appendChild(cursorFollower);

    let mouseX = 0;
    let mouseY = 0;
    let followerX = 0;
    let followerY = 0;

    // Mouse movement
    document.addEventListener('mousemove', (e) => {
        mouseX = e.clientX;
        mouseY = e.clientY;

        // Main dot follows instantly
        cursor.style.left = mouseX + 'px';
        cursor.style.top = mouseY + 'px';
    });

    // Smooth animation loop
    function animate() {
        // Interpolation (lerp) for smooth movement
        // Increase 0.1 to make it faster/less laggy, decrease for more delay
        followerX += (mouseX - followerX) * 0.2;
        followerY += (mouseY - followerY) * 0.2;

        cursorFollower.style.left = followerX + 'px';
        cursorFollower.style.top = followerY + 'px';

        requestAnimationFrame(animate);
    }
    animate();

    // Hover effects on interactive elements
    const handleHover = () => {
        const links = document.querySelectorAll('a, button, .nav-item, .social-icon');
        links.forEach(link => {
            // Remove previous listeners to avoid duplicates
            link.removeEventListener('mouseenter', addHoverParams);
            link.removeEventListener('mouseleave', removeHoverParams);

            link.addEventListener('mouseenter', addHoverParams);
            link.addEventListener('mouseleave', removeHoverParams);
        });
    };

    const addHoverParams = () => {
        cursor.classList.add('hovered');
        cursorFollower.classList.add('hovered');
    };

    const removeHoverParams = () => {
        cursor.classList.remove('hovered');
        cursorFollower.classList.remove('hovered');
    };

    // Initial attach
    handleHover();

    // Re-attach listeners for dynamically added content
    const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
            if (mutation.addedNodes.length) {
                handleHover();
            }
        });
    });

    observer.observe(document.body, { childList: true, subtree: true });
});
