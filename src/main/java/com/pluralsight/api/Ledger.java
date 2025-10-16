package com.pluralsight.api;

import com.pluralsight.api.Transaction;
import com.pluralsight.ledger.CsvUtil;
import com.pluralsight.ledger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ledger Service
 * ------------------------------------------------------------
 * Provides CRUD-style methods to manage transactions
 * and supports persistence via CSV file.
 */
public class Ledger {

    private final List<Transaction> transactions = new ArrayList<>();
    private final String CSV_PATH = "data/transactions.csv";

    public Ledger() {
        transactions.addAll(CsvUtil.readTransactions(CSV_PATH));
        Logger.log("Ledger initialized with " + transactions.size() + " records.");
    }

    public synchronized Transaction add(Transaction t) {
        transactions.add(t);
        CsvUtil.writeTransactions(CSV_PATH, transactions);
        Logger.log("Added transaction: " + t);
        return t;
    }

    public List<Transaction> getAll() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getDeposits() {
        return transactions.stream()
                .filter(t -> t.getAmount() > 0)
                .collect(Collectors.toList());
    }

    public List<Transaction> getPayments() {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.toList());
    }
}