import { Api } from './api.js';
import { notify } from './notifications.js';

export async function loadReport(period, tableId) {
    try {
        const data = await Api.getReports(period);
        const tbody = document.querySelector(`#${tableId} tbody`);
        tbody.innerHTML = '';

        if (!data.length) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">No data</td></tr>';
            return;
        }

        let total = 0;
        data.forEach(tx => {
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

        const totalElem = document.querySelector(`#${tableId}-total`);
        if (totalElem) totalElem.textContent = total.toFixed(2);
    } catch (err) {
        notify(`Failed to load ${period} report`, 'error');
    }
}