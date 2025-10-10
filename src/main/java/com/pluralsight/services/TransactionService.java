package com.pluralsight.services;

import com.pluralsight.models.Transaction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TransactionService {
    private final List<Transaction> transactions = Collections.synchronizedList(new LinkedList<>());

    public TransactionService() {
        // Preload with demo data
        transactions.add(new Transaction("Opening Balance", "System", 1000.00));
        transactions.add(new Transaction("Office Supplies", "Staples", -150.75));
        transactions.add(new Transaction("Client Payment", "ACME Corp", 500.00));
    }

    public List<Transaction> getAll() {
        return new LinkedList<>(transactions);
    }

    public Transaction addTransaction(String description, String vendor, double amount) {
        Transaction transaction = new Transaction(description, vendor, amount);
        transactions.add(0, transaction);
        return transaction;
    }
}