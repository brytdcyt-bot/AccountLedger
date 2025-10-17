// --- Fetch and display transactions ---
async function loadTransactions(filter = 'all', vendorName = '') {
    const res = await fetch('/api/transactions');
    const transactions = await res.json();

    let filtered = transactions;
    if (filter === 'deposits') filtered = transactions.filter(t => t.amount > 0);
    if (filter === 'payments') filtered = transactions.filter(t => t.amount < 0);
    if (filter === 'vendor') filtered = transactions.filter(t => t.vendor.toLowerCase() === vendorName.toLowerCase());

    const tbody = document.getElementById('transactionsBody');
    tbody.innerHTML = '';
    filtered.forEach(t => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${t.date}</td>
            <td>${t.time}</td>
            <td>${t.description}</td>
            <td>${t.vendor}</td>
            <td>${t.amount.toFixed(2)}</td>
        `;
        tbody.appendChild(row);
    });
}

// --- Add transaction ---
document.getElementById('addBtn').addEventListener('click', async () => {
    const description = document.getElementById('description').value;
    const vendor = document.getElementById('vendor').value;
    const amount = parseFloat(document.getElementById('amount').value);
    const date = document.getElementById('date').value; // optional
    const time = document.getElementById('time').value; // optional

    if (!description || !vendor || isNaN(amount)) {
        alert('Please fill out description, vendor, and amount.');
        return;
    }

    const transaction = { description, vendor, amount };
    if (date) transaction.date = date;
    if (time) transaction.time = time;

    const res = await fetch('/api/transactions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(transaction)
    });

    if (res.ok) {
        document.getElementById('description').value = '';
        document.getElementById('vendor').value = '';
        document.getElementById('amount').value = '';
        document.getElementById('date').value = '';
        document.getElementById('time').value = '';
        loadTransactions();
    } else {
        alert('Failed to add transaction.');
    }
});

// --- Filter buttons ---
document.getElementById('allBtn').addEventListener('click', () => loadTransactions('all'));
document.getElementById('depositsBtn').addEventListener('click', () => loadTransactions('deposits'));
document.getElementById('paymentsBtn').addEventListener('click', () => loadTransactions('payments'));
document.getElementById('vendorBtn').addEventListener('click', () => {
    const vendor = document.getElementById('vendorFilter').value;
    if (!vendor) return alert('Enter vendor name.');
    loadTransactions('vendor', vendor);
});

// --- Initial load ---
loadTransactions();