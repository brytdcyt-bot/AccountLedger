package com.pluralsight.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single financial transaction in the ledger.
 */
public class Transaction {
    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final String vendor;
    private final double amount;

    public Transaction(String description, String vendor, double amount) {
        this(LocalDate.now(), LocalTime.now(), description, vendor, amount);
    }

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getDescription() { return description; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("%s | %s | %-20s | %-15s | %10.2f",
                date.format(DateTimeFormatter.ISO_DATE),
                time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                description, vendor, amount);
    }

    public String toCsv() {
        return String.join(",",
                date.toString(),
                time.toString(),
                "\"" + description.replace("\"", "\"\"") + "\"",
                "\"" + vendor.replace("\"", "\"\"") + "\"",
                String.valueOf(amount));
    }

    public static Transaction fromCsv(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length < 5) return null;
        return new Transaction(
                LocalDate.parse(parts[0]),
                LocalTime.parse(parts[1]),
                parts[2].replace("\"", ""),
                parts[3].replace("\"", ""),
                Double.parseDouble(parts[4])
        );
    }

    public void setDate(String format) {

    }
}