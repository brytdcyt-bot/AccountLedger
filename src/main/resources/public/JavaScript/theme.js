export function initTheme(defaultMode = 'light') {
    if (defaultMode === 'dark') {
        document.body.classList.add('dark-mode');
    } else {
        document.body.classList.remove('dark-mode');
    }
}

export function toggleTheme() {
    document.body.classList.toggle('dark-mode');
}