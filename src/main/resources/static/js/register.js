document.addEventListener('DOMContentLoaded', () => {
    const showSuccess = /*[[${showSuccessModal}]]*/ false;
    console.log("showSuccess value:", showSuccess);  // Debug check

    if (showSuccess) {
        const modalElement = document.getElementById('successModal');
        if (modalElement) {
            const modal = new bootstrap.Modal(modalElement);
            modal.show();
        } else {
            console.log("Modal element not found!");
        }
    }
});
