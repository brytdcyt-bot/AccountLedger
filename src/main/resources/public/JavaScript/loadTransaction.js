import { Api } from './api.js';

export async function loadLedger() {
    const tbody = document.querySelector('#ledgerTable tbody');
    const totalElem = document.querySelector('#totalAmount');
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Loading...</td></tr>';

    try {
        const transactions = await Api.getAll();
        if (!transactions.length) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">No transactions yet</td></tr>';
            totalElem.textContent = '0.00';
            return;
        }

        let total = 0;
        tbody.innerHTML = '';
        transactions.forEach(tx => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${tx.date}</td>
                <td>${tx.time || ''}</td>
                <td>${tx.description}</td>
                <td>${tx.vendor}</td>
                <td class="${tx.amount >= 0 ? 'amount-positive' : 'amount-negative'}">
                    ${tx.amount.toFixed(2)}
                </td>
            `;
            tbody.appendChild(tr);
            total += tx.amount;
        });

        totalElem.textContent = total.toFixed(2);
    } catch (err) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:red;">Failed to load ledger</td></tr>';
    }
}