async function loadTransactions() {
    const loader = document.getElementById('loader');
    const table = document.getElementById('ledgerTable');
    const tbody = document.getElementById('ledgerBody');

    loader.classList.remove('hidden');
    table.classList.add('hidden');
    tbody.innerHTML = '';

    try {
        const res = await fetch('/api/transactions');
        if (!res.ok) throw new Error('Failed to load transactions.');
        const transactions = await res.json();

        if (!Array.isArray(transactions) || transactions.length === 0) {
            loader.textContent = 'No transactions found.';
            return;
        }

        transactions.forEach(t => {
            const tr = document.createElement('tr');

            const date = new Date(t.dateTime).toLocaleString();
            const amountClass = t.amount >= 0 ? 'amount-positive' : 'amount-negative';

            tr.innerHTML = `
        <td>${date}</td>
        <td>${t.description}</td>
        <td>${t.vendor}</td>
        <td class="right ${amountClass}">${t.amount.toFixed(2)}</td>
      `;
            tbody.appendChild(tr);
        });

        loader.classList.add('hidden');
        table.classList.remove('hidden');
    } catch (err) {
        loader.textContent = 'Error loading transactions.';
        console.error(err);
    }
}

document.getElementById('addForm').addEventListener('submit', async e => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const params = new URLSearchParams(formData);

    const amount = parseFloat(formData.get('amount'));
    if (isNaN(amount)) {
        alert('Amount must be a valid number.');
        return;
    }

    try {
        const res = await fetch('/api/add?' + params.toString(), { method: 'POST' });
        if (!res.ok) throw new Error('Failed to add transaction.');
        e.target.reset();
        await loadTransactions();
    } catch (err) {
        console.error(err);
        alert('Error saving transaction.');
    }
});

document.addEventListener('DOMContentLoaded', loadTransactions);
