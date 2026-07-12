/**
 * Accounting Ledger Application
 * Main application logic for managing transactions
 */

class AccountingLedger {
    constructor() {
        this.transactions = [];
        this.currentFilter = 'all';
        this.vendorSearch = '';
        this.apiUrl = '/api/transactions';
        this.init();
    }

    /**
     * Initialize the application
     */
    init() {
        this.setupEventListeners();
        this.setDefaultDate();
        this.loadTransactions();
    }

    /**
     * Set up all event listeners
     */
    setupEventListeners() {
        // Form submission
        document.getElementById('transactionForm').addEventListener('submit', (e) => this.handleFormSubmit(e));

        // Filter buttons
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.handleFilterChange(e));
        });

        // Vendor search
        document.getElementById('vendorSearch').addEventListener('input', (e) => this.handleVendorSearch(e));
    }

    /**
     * Set today's date as default in the date input
     */
    setDefaultDate() {
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('transactionDate').value = today;
    }

    /**
     * Handle form submission
     */
    handleFormSubmit(e) {
        e.preventDefault();

        const formData = {
            date: document.getElementById('transactionDate').value,
            amount: parseFloat(document.getElementById('transactionAmount').value),
            type: document.getElementById('transactionType').value,
            vendor: document.getElementById('transactionVendor').value.trim()
        };

        // Validation
        if (!this.validateTransaction(formData)) {
            return;
        }

        // Add transaction
        this.addTransaction(formData);
        this.resetForm();
    }

    /**
     * Validate transaction data
     */
    validateTransaction(data) {
        const messageEl = document.getElementById('formMessage');

        if (!data.date) {
            this.showMessage('Please select a date', 'error', messageEl);
            return false;
        }

        if (isNaN(data.amount) || data.amount <= 0) {
            this.showMessage('Amount must be a positive number', 'error', messageEl);
            return false;
        }

        if (!data.vendor || data.vendor.length === 0) {
            this.showMessage('Please enter a vendor or description', 'error', messageEl);
            return false;
        }

        // Prevent CSV injection
        if (/["'\n\r]/g.test(data.vendor)) {
            this.showMessage('Vendor name contains invalid characters', 'error', messageEl);
            return false;
        }

        return true;
    }

    /**
     * Add a new transaction
     */
    addTransaction(transaction) {
        this.transactions.push({
            id: Date.now(),
            ...transaction
        });

        this.saveTransactions();
        this.render();
        this.showMessage('Transaction added successfully!', 'success', document.getElementById('formMessage'));
    }

    /**
     * Delete a transaction
     */
    deleteTransaction(id) {
        if (confirm('Are you sure you want to delete this transaction?')) {
            this.transactions = this.transactions.filter(t => t.id !== id);
            this.saveTransactions();
            this.render();
        }
    }

    /**
     * Save transactions to local storage
     */
    saveTransactions() {
        localStorage.setItem('accountingLedger', JSON.stringify(this.transactions));
    }

    /**
     * Load transactions from local storage or API
     */
    loadTransactions() {
        const stored = localStorage.getItem('accountingLedger');
        if (stored) {
            this.transactions = JSON.parse(stored);
        }
        this.render();
    }

    /**
     * Handle filter button change
     */
    handleFilterChange(e) {
        document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
        e.target.classList.add('active');
        this.currentFilter = e.target.dataset.filter;
        this.render();
    }

    /**
     * Handle vendor search input
     */
    handleVendorSearch(e) {
        this.vendorSearch = e.target.value.toLowerCase();
        this.render();
    }

    /**
     * Get filtered transactions
     */
    getFilteredTransactions() {
        return this.transactions.filter(transaction => {
            // Filter by type
            if (this.currentFilter !== 'all' && transaction.type !== this.currentFilter) {
                return false;
            }

            // Filter by vendor search
            if (this.vendorSearch && !transaction.vendor.toLowerCase().includes(this.vendorSearch)) {
                return false;
            }

            return true;
        });
    }

    /**
     * Calculate summary totals
     */
    calculateTotals() {
        const filtered = this.getFilteredTransactions();
        const totalDeposits = filtered
            .filter(t => t.type === 'deposit')
            .reduce((sum, t) => sum + t.amount, 0);
        const totalPayments = filtered
            .filter(t => t.type === 'payment')
            .reduce((sum, t) => sum + t.amount, 0);

        return {
            deposits: totalDeposits,
            payments: totalPayments,
            balance: totalDeposits - totalPayments
        };
    }

    /**
     * Render the application
     */
    render() {
        this.renderSummary();
        this.renderTransactionsTable();
    }

    /**
     * Render summary cards
     */
    renderSummary() {
        const totals = this.calculateTotals();
        document.getElementById('totalDeposits').textContent = this.formatCurrency(totals.deposits);
        document.getElementById('totalPayments').textContent = this.formatCurrency(totals.payments);

        const balanceEl = document.getElementById('netBalance');
        balanceEl.textContent = this.formatCurrency(totals.balance);
        balanceEl.className = 'summary-value ' + (totals.balance >= 0 ? 'positive' : 'negative');
    }

    /**
     * Render transactions table
     */
    renderTransactionsTable() {
        const filtered = this.getFilteredTransactions();
        const table = document.getElementById('transactionsTable');
        const tbody = document.getElementById('transactionsBody');
        const noTransactions = document.getElementById('noTransactions');

        if (filtered.length === 0) {
            table.style.display = 'none';
            noTransactions.style.display = 'block';
            return;
        }

        table.style.display = 'table';
        noTransactions.style.display = 'none';

        tbody.innerHTML = filtered.map(transaction => `
            <tr class="transaction-row ${transaction.type}">
                <td>${this.formatDate(transaction.date)}</td>
                <td class="amount">${this.formatCurrency(transaction.amount)}</td>
                <td><span class="badge badge-${transaction.type}">${this.capitalizeFirst(transaction.type)}</span></td>
                <td>${this.escapeHtml(transaction.vendor)}</td>
                <td>
                    <button class="btn-delete" onclick="ledger.deleteTransaction(${transaction.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    }

    /**
     * Format currency
     */
    formatCurrency(amount) {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    }

    /**
     * Format date
     */
    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    }

    /**
     * Capitalize first letter
     */
    capitalizeFirst(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }

    /**
     * Escape HTML to prevent XSS
     */
    escapeHtml(text) {
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, m => map[m]);
    }

    /**
     * Show message to user
     */
    showMessage(message, type, element) {
        element.textContent = message;
        element.className = `message ${type}`;
        setTimeout(() => {
            element.textContent = '';
            element.className = 'message';
        }, 4000);
    }

    /**
     * Reset form
     */
    resetForm() {
        document.getElementById('transactionForm').reset();
        this.setDefaultDate();
    }
}

// Initialize the application when DOM is ready
let ledger;
document.addEventListener('DOMContentLoaded', () => {
    ledger = new AccountingLedger();
});