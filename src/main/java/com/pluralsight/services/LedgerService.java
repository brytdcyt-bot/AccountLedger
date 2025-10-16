package com.pluralsight.services;

import com.pluralsight.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for ledger operations.
 * Handles transactions storage and filtering.
 */
@Service
public class LedgerService {

    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Returns all transactions.
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Adds a new transaction to the ledger.
     */
    public Transaction add(Transaction t) {
        transactions.add(t);
        return t;
    }

    /**
     * Returns only deposits (positive amounts).
     */
    public List<Transaction> getDeposits() {
        return transactions.stream()
                .filter(tx -> tx.getAmount() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Returns only payments (negative amounts).
     */
    public List<Transaction> getPayments() {
        return transactions.stream()
                .filter(tx -> tx.getAmount() < 0)
                .collect(Collectors.toList());
    }
}
