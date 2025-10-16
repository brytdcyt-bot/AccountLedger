export function filterTransactions(transactions, criteria) {
    return transactions.filter(tx => {
        if (criteria.vendor && tx.vendor.toLowerCase() !== criteria.vendor.toLowerCase()) return false;
        if (criteria.minAmount && tx.amount < criteria.minAmount) return false;
        if (criteria.maxAmount && tx.amount > criteria.maxAmount) return false;
        return true;
    });
}
