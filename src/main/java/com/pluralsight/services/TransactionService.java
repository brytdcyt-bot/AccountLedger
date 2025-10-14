package com.pluralsight.services;

import com.pluralsight.models.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * Handles all transaction operations and reporting features.
 * Provides a clean API layer between CLI/Web interfaces and the underlying data model.
 */
public class TransactionService {

    private final Ledger ledger;
    private final Reports reports;

    public TransactionService() {
        this.ledger = new Ledger(CsvUtil.readTransactions());
        this.reports = new Reports(ledger);
        Logger.info("TransactionService initialized.");
    }

    /* ──────────────────────────────
       TRANSACTION MANAGEMENT
    ────────────────────────────── */

    /** Add a new deposit (amount > 0) */
    public Transaction addDeposit(String description, String vendor, double amount) {
        validateInput(description, vendor, amount, true);
        Transaction t = createTransaction(description, vendor, amount);
        ledger.add(t);
        Logger.info("Deposit added: " + t);
        return t;
    }

    /** Add a new payment (amount < 0) */
    public Transaction addPayment(String description, String vendor, double amount) {
        validateInput(description, vendor, amount, true);
        Transaction t = createTransaction(description, vendor, -Math.abs(amount));
        ledger.add(t);
        Logger.info("Payment added: " + t);
        return t;
    }

    /** Fetch all transactions (sorted newest first) */
    public List<Transaction> getAllTransactions() {
        return ledger.getAll();
    }

    /** Fetch deposits only (amount > 0) */
    public List<Transaction> getDeposits() {
        return ledger.getDeposits();
    }

    /** Fetch payments only (amount < 0) */
    public List<Transaction> getPayments() {
        return ledger.getPayments();
    }

    /** Filter by vendor name */
    public List<Transaction> getByVendor(String vendor) {
        return ledger.getByVendor(Objects.requireNonNullElse(vendor, ""));
    }

    /* ──────────────────────────────
       REPORTING FEATURES
    ────────────────────────────── */

    public List<Transaction> monthToDate() { return reports.monthToDate(); }
    public List<Transaction> previousMonth() { return reports.previousMonth(); }
    public List<Transaction> yearToDate() { return reports.yearToDate(); }
    public List<Transaction> previousYear() { return reports.previousYear(); }

    /** Flexible custom search for advanced filtering */
    public List<Transaction> customSearch(LocalDate start, LocalDate end, String vendor, String keyword) {
        return reports.customSearch(start, end, vendor, keyword);
    }

    /* ──────────────────────────────
       INTERNAL UTILITIES
    ────────────────────────────── */

    /** Persist all transactions to CSV */
    public void save() {
        ledger.save();
        Logger.info("Ledger saved to disk.");
    }

    /** Create a transaction with current timestamp */
    private Transaction createTransaction(String description, String vendor, double amount) {
        return new Transaction(
                LocalDate.now().toString(),
                LocalTime.now().withNano(0).toString(),
                description,
                vendor,
                amount
        );
    }

    /** Validate common input fields */
    private void validateInput(String description, String vendor, double amount, boolean requirePositive) {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description is required.");
        if (vendor == null || vendor.isBlank())
            throw new IllegalArgumentException("Vendor is required.");
        if (requirePositive && amount <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
    }
}