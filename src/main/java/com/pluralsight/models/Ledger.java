package com.pluralsight.models;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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
        System.out.println("Ledger initialized with " + transactions.size() + " transactions.");
    }

    /** Add a new transaction and persist immediately */
    public synchronized void add(Transaction transaction) {
        if (transaction == null) {
            System.out.println("Attempted to add null transaction — ignored.");
            return;
        }

        transactions.add(transaction);
        save(); // single point of persistence
        System.out.println("Transaction added: " + transaction);
    }

    /** Add a few test transactions for quick testing */
    public void addTestTransactions() {
        add(new Transaction("2025-10-14", "Test Deposit", 100.00, "TestVendor"));
        add(new Transaction("2025-10-14", "Test Withdrawal", -50.00, "TestVendor"));
        add(new Transaction("2025-10-15", "Coffee", -4.50, "CafeX"));
        add(new Transaction("2025-10-15", "Salary", 2000.00, "EmployerInc"));
        System.out.println("Test transactions added.");
    }

    /** Return all transactions (newest first, immutable) */
    public List<Transaction> getAll() {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
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
        System.out.println("Ledger saved (" + transactions.size() + " entries).");
    }

    /** Optional: simple console helper to manually add a transaction */
    public void interactiveAdd() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        System.out.print("Enter description: ");
        String desc = sc.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = sc.nextLine();
        System.out.print("Enter amount: ");
        double amt = sc.nextDouble();

        add(new Transaction(date, desc, amt, vendor));
        System.out.println("Transaction added via console.");
    }
}
