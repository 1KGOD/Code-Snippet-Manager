// Animate floating circles
document.querySelectorAll('.floating-circles span').forEach((circle, i) => {
    circle.style.animationDelay = `${i * 0.3}s`;
});
