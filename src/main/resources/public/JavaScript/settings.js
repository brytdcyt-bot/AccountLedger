import { notify } from './notifications.js';
import { Storage } from './storage.js';
import { initTheme, toggleTheme } from './theme.js';

export const Settings = {
    load() {
        const settings = Storage.get('settings') || {};
        if (settings.theme === 'dark') initTheme('dark');
        document.querySelector('#themeToggle').checked = settings.theme === 'dark';
    },

    save(settings) {
        Storage.set('settings', settings);
        notify('Settings saved', 'success');
    },

    toggleTheme() {
        const current = Storage.get('settings') || {};
        current.theme = current.theme === 'dark' ? 'light' : 'dark';
        Storage.set('settings', current);
        toggleTheme();
        notify(`Theme switched to ${current.theme}`, 'info');
    }
};