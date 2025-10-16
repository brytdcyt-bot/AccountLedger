import { loadLedger } from './loadTransaction.js';
import { initTheme } from './theme.js';
import { setupTransactionForm } from './app.js';

document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    setupTransactionForm();
    loadLedger();
});