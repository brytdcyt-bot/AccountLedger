export function validateTransaction(tx) {
    if (!tx.description || !tx.vendor || isNaN(tx.amount)) return false;
    return true;
}