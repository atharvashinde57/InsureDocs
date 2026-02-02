document.addEventListener('DOMContentLoaded', () => {
    // Check if cursor already exists
    if (!document.getElementById('custom-cursor')) {
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

        document.addEventListener('mousemove', (e) => {
            mouseX = e.clientX;
            mouseY = e.clientY;
            cursor.style.left = mouseX + 'px';
            cursor.style.top = mouseY + 'px';
        });

        function animate() {
            followerX += (mouseX - followerX) * 0.2;
            followerY += (mouseY - followerY) * 0.2;
            cursorFollower.style.left = followerX + 'px';
            cursorFollower.style.top = followerY + 'px';
            requestAnimationFrame(animate);
        }
        animate();

        const handleHover = () => {
            const links = document.querySelectorAll('a, button, input, select, .nav-item, .social-icon');
            links.forEach(link => {
                link.addEventListener('mouseenter', () => {
                    cursor.classList.add('hovered');
                    cursorFollower.classList.add('hovered');
                });
                link.addEventListener('mouseleave', () => {
                    cursor.classList.remove('hovered');
                    cursorFollower.classList.remove('hovered');
                });
            });
        };

        handleHover();

        // Re-attach for dynamic content
        const observer = new MutationObserver((mutations) => {
            handleHover();
        });
        observer.observe(document.body, { childList: true, subtree: true });
    }
});
