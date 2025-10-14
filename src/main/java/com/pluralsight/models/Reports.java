package com.pluralsight.models;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Reports {

    private final Ledger ledger;

    public Reports(Ledger ledger) {
        this.ledger = ledger;
    }

    /** Month-to-Date Report */
    public List<Transaction> monthToDate() {
        Logger.log("Generating Month-to-Date report...");
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        return getRange(start, LocalDate.now());
    }

    /** Previous Month Report */
    public List<Transaction> previousMonth() {
        Logger.log("Generating Previous Month report...");
        LocalDate start = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return getRange(start, end);
    }

    /** Year-to-Date Report */
    public List<Transaction> yearToDate() {
        Logger.log("Generating Year-to-Date report...");
        LocalDate start = LocalDate.now().withDayOfYear(1);
        return getRange(start, LocalDate.now());
    }

    /** Previous Year Report */
    public List<Transaction> previousYear() {
        Logger.log("Generating Previous Year report...");
        LocalDate start = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate end = start.withMonth(12).withDayOfMonth(31);
        return getRange(start, end);
    }

    /** Search by Vendor */
    public List<Transaction> searchByVendor(String vendor) {
        if (vendor == null || vendor.isBlank()) {
            Logger.log("Vendor search skipped (empty input).");
            return List.of();
        }
        Logger.log("Searching transactions by vendor: " + vendor);
        return ledger.getByVendor(vendor);
    }

    /** Custom Search with optional filters */
    public List<Transaction> customSearch(LocalDate start, LocalDate end, String vendor, String keyword) {
        Logger.log("Performing custom search with filters: " +
                "Start=" + start + ", End=" + end +
                ", Vendor=" + vendor + ", Keyword=" + keyword);

        return ledger.getAll().stream()
                .filter(t -> start == null || !t.getLocalDate().isBefore(start))
                .filter(t -> end == null || !t.getLocalDate().isAfter(end))
                .filter(t -> vendor == null || vendor.isBlank() ||
                        t.getVendor().equalsIgnoreCase(vendor))
                .filter(t -> keyword == null || keyword.isBlank() ||
                        t.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    /** Calculate total income (amount > 0) */
    public double totalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /** Calculate total expenses (amount < 0) */
    public double totalExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /** Get a summary string of any report */
    public String generateSummary(List<Transaction> transactions) {
        double income = totalIncome(transactions);
        double expenses = totalExpenses(transactions);
        double net = income + expenses;

        String summary = String.format(
                "Report Summary: %d transactions | Income: $%.2f | Expenses: $%.2f | Net: $%.2f",
                transactions.size(), income, Math.abs(expenses), net
        );

        Logger.log(summary);
        return summary;
    }

    /** Internal helper for reusable date-range logic */
    private List<Transaction> getRange(LocalDate start, LocalDate end) {
        return ledger.getByDateRange(start, end);
    }
}