import { Api } from './api.js';
import { notify } from './notifications.js';

export const Server = {
    start() {
        console.log('Server simulation started. API ready at /api/transactions');
        notify('Server running (simulated)', 'info');
    },

    async simulateAddTransaction(tx) {
        try {
            const added = await Api.addTransaction(tx);
            notify('Transaction added on server', 'success');
            return added;
        } catch (err) {
            notify('Server failed to add transaction', 'error');
        }
    }
};