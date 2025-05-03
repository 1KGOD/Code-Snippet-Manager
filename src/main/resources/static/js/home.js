document.addEventListener("DOMContentLoaded", () => {
    const profileDropdown = document.querySelector(".profile-dropdown");
    const dropdownMenu = document.querySelector(".dropdown-menu");

    profileDropdown.addEventListener("click", (e) => {
      e.stopPropagation();
      dropdownMenu.classList.toggle("active");
    });

    document.addEventListener("click", () => {
      dropdownMenu.classList.remove("active");
    });
  });