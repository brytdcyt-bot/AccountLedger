export const UI = {
    showLoader(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;
        container.innerHTML = '<div class="loader">Loading...</div>';
    },

    hideLoader(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;
        container.innerHTML = '';
    },

    createModal(title, content) {
        const modal = document.createElement('div');
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content">
                <h2>${title}</h2>
                <div class="modal-body">${content}</div>
                <button class="btn close-modal">Close</button>
            </div>
        `;
        document.body.appendChild(modal);
        modal.querySelector('.close-modal').addEventListener('click', () => modal.remove());
        return modal;
    },

    tooltip(element, message) {
        element.addEventListener('mouseenter', () => {
            const tip = document.createElement('span');
            tip.className = 'tooltip';
            tip.textContent = message;
            element.appendChild(tip);
        });
        element.addEventListener('mouseleave', () => {
            const tip = element.querySelector('.tooltip');
            if (tip) tip.remove();
        });
    }
};