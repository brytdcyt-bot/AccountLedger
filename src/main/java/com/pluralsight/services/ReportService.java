package com.pluralsight.services;

import com.pluralsight.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides filtering utilities for transactions,
 * used by Reports and Ledger analysis modules.
 */
public class ReportService {

    /**
     * Filters transactions that occur within the given date range (inclusive).
     *
     * @param transactions list of all transactions
     * @param start        start date (inclusive)
     * @param end          end date (inclusive)
     * @return filtered list of transactions
     */
    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate start, LocalDate end) {
        if (transactions == null || transactions.isEmpty()) return List.of();
        if (start == null && end == null) return List.copyOf(transactions);

        return transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> {
                    LocalDate date = t.getLocalDate();
                    boolean afterStart = (start == null) || !date.isBefore(start);
                    boolean beforeEnd = (end == null) || !date.isAfter(end);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }

    /**
     * Filters transactions by a specific vendor name.
     *
     * @param transactions list of all transactions
     * @param vendor       vendor name (case-insensitive)
     * @return list of transactions from that vendor
     */
    public List<Transaction> filterByVendor(List<Transaction> transactions, String vendor) {
        if (transactions == null || vendor == null || vendor.isBlank()) return List.of();

        String vendorLower = vendor.trim().toLowerCase();

        return transactions.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getVendor() != null && t.getVendor().equalsIgnoreCase(vendorLower))
                .collect(Collectors.toList());
    }
}