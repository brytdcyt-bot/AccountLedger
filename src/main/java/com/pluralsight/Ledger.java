package com.pluralsight.models;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Ledger {

    private final List<Transaction> transactions;

    /** Initialize ledger by reading CSV */
    public Ledger(List<Transaction> initialTransactions) {
        if (initialTransactions != null) {
            // CopyOnWriteArrayList = safe for web + concurrent access
            this.transactions = new CopyOnWriteArrayList<>(initialTransactions);
        } else {
            this.transactions = new CopyOnWriteArrayList<>();
        }
        Logger.log("Ledger initialized with " + transactions.size() + " transactions.");
    }

    /** Add a new transaction and persist immediately */
    public synchronized void add(Transaction transaction) {
        if (transaction == null) {
            Logger.log("Attempted to add null transaction — ignored.");
            return;
        }

        transactions.add(transaction);
        save(); // single point of persistence
        Logger.log("Transaction added: " + transaction);
    }

    /** Return all transactions (newest first, immutable) */
    public List<Transaction> getAll() {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed()
                        .thenComparing(Transaction::getTime).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    /** Return deposits only (amount > 0, newest first) */
    public List<Transaction> getDeposits() {
        return transactions.stream()
                .filter(t -> t.getAmount() > 0)
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    /** Return payments only (amount < 0, newest first) */
    public List<Transaction> getPayments() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    /** Filter transactions by vendor (case-insensitive, newest first) */
    public List<Transaction> getByVendor(String vendor) {
        if (vendor == null || vendor.isBlank()) return List.of();

        return transactions.stream()
                .filter(t -> t.getVendor() != null && t.getVendor().equalsIgnoreCase(vendor))
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    /** Persist all transactions to CSV */
    public synchronized void save() {
        CsvUtil.writeTransactions(transactions);
        Logger.log("Ledger saved (" + transactions.size() + " entries).");
    }
}