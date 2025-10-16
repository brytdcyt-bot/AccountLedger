export function formatCurrency(amount) {
    return amount.toLocaleString(undefined, { style: 'currency', currency: 'USD' });
}

export function formatDate(dateStr) {
    return new Date(dateStr).toLocaleDateString();
}
