export interface Transaction {
    date: string;       // YYYY-MM-DD
    time: string;       // HH:mm:ss
    description: string;
    vendor: string;
    amount: number;
}

export class Ledger {
    private transactions: Transaction[] = [];

    constructor(initialTransactions?: Transaction[]) {
        if (initialTransactions) this.transactions = [...initialTransactions];
    }

    addTransaction(tx: Transaction): void {
        if (!tx.date || !tx.time || !tx.description || !tx.vendor || typeof tx.amount !== "number") {
            throw new Error("Invalid transaction object");
        }
        this.transactions.push(tx);
        this.transactions.sort((a, b) => (a.date + a.time).localeCompare(b.date + b.time));
    }

    removeTransaction(index: number): void {
        if (index >= 0 && index < this.transactions.length) {
            this.transactions.splice(index, 1);
        } else {
            console.warn("Invalid transaction index:", index);
        }
    }

    getAll(): Transaction[] {
        return [...this.transactions];
    }

    getTotal(): number {
        return this.transactions.reduce((sum, tx) => sum + tx.amount, 0);
    }

    getDeposits(): Transaction[] {
        return this.transactions.filter(tx => tx.amount > 0);
    }

    getPayments(): Transaction[] {
        return this.transactions.filter(tx => tx.amount < 0);
    }
}

// Example usage
const ledger = new Ledger();
ledger.addTransaction({
    date: "2025-10-16",
    time: "10:00:00",
    description: "Client Payment",
    vendor: "ABC Corp",
    amount: 1500
});

console.log("Total Balance:", ledger.getTotal());
console.log("All Transactions:", ledger.getAll());
