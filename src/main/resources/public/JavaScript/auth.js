import { notify } from './notifications.js';

export const Auth = {
    isLoggedIn: false,
    user: null,

    async login(username, password) {
        try {
            const res = await httpRequest('/api/login', 'POST', { username, password });
            this.isLoggedIn = true;
            this.user = res.user;
            notify('Logged in successfully', 'success');
            return res.user;
        } catch (err) {
            notify('Login failed', 'error');
            throw err;
        }
    },

    logout() {
        this.isLoggedIn = false;
        this.user = null;
        notify('Logged out', 'info');
    }
};