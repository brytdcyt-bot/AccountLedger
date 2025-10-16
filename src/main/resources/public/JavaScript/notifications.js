export function notify(message, type = 'info', duration = 3000) {
    const container = document.getElementById('notification-container') || createContainer();
    const notif = document.createElement('div');
    notif.className = `notification ${type}`;
    notif.textContent = message;
    container.appendChild(notif);
    setTimeout(() => notif.remove(), duration);
}

function createContainer() {
    const container = document.createElement('div');
    container.id = 'notification-container';
    container.style.position = 'fixed';
    container.style.top = '1rem';
    container.style.right = '1rem';
    container.style.zIndex = 9999;
    document.body.appendChild(container);
    return container;
}
