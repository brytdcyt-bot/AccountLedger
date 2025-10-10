package com.pluralsight;

import com.pluralsight.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Reports {
    private final Ledger ledger;

    public Reports(Ledger ledger) {
        this.ledger = ledger;
    }

    public List<Transaction> monthToDate() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        return ledger.getByDateRange(start, LocalDate.now());
    }

    public List<Transaction> previousMonth() {
        LocalDate start = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return ledger.getByDateRange(start, end);
    }

    public List<Transaction> yearToDate() {
        LocalDate start = LocalDate.now().withDayOfYear(1);
        return ledger.getByDateRange(start, LocalDate.now());
    }

    public List<Transaction> previousYear() {
        LocalDate start = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate end = start.withMonth(12).withDayOfMonth(31);
        return ledger.getByDateRange(start, end);
    }

    public List<Transaction> searchByVendor(String vendor) {
        return ledger.getByVendor(vendor);
    }

    public List<Transaction> customSearch(LocalDate start, LocalDate end, String vendor, String keyword) {
        return ledger.getAll().stream()
                .filter(t -> start == null || !t.getLocalDate().isBefore(start))
                .filter(t -> end == null || !t.getLocalDate().isAfter(end))
                .filter(t -> vendor == null || t.getVendor().equalsIgnoreCase(vendor))
                .filter(t -> keyword == null || t.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}