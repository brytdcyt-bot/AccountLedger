package com.pluralsight;

import com.pluralsight.models.Transaction;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Ledger {
    private final List<Transaction> transactions;

    public Ledger(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public List<Transaction> getAll() {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getLocalDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> getDeposits() {
        return transactions.stream().filter(t -> t.getAmount() > 0).toList();
    }

    public List<Transaction> getPayments() {
        return transactions.stream().filter(t -> t.getAmount() < 0).toList();
    }

    public List<Transaction> getByVendor(String vendor) {
        return transactions.stream()
                .filter(t -> t.getVendor().equalsIgnoreCase(vendor))
                .toList();
    }

    public void add(Transaction t) {
        transactions.add(t);
        save();
    }

    public void save() {
        CsvUtil.writeTransactions(transactions);
    }

    public List<Transaction> getByDateRange(LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(t -> {
                    LocalDate d = t.getLocalDate();
                    return (d.isEqual(start) || d.isAfter(start)) && (d.isEqual(end) || d.isBefore(end));
                })
                .toList();
    }
}