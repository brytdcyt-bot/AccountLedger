import { Api } from './api.js';
import { loadLedger } from './loadTransaction.js';
import { notify } from './notifications.js';
import { validateTransaction } from './validator.js';

document.addEventListener('DOMContentLoaded', () => {
    loadLedger();
    setupTransactionForm();
});

function setupTransactionForm() {
    const form = document.querySelector('#transactionForm');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const tx = {
            description: form.description.value.trim(),
            vendor: form.vendor.value.trim(),
            amount: parseFloat(form.amount.value)
        };

        if (!validateTransaction(tx)) {
            notify('Invalid transaction data', 'error');
            return;
        }

        try {
            await Api.addTransaction(tx);
            notify('Transaction added successfully', 'success');
            form.reset();
            loadLedger();
        } catch {
            notify('Failed to add transaction', 'error');
        }
    });
}