package com.pluralsight.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a financial transaction in the ledger system.
 */
public class Transaction {

    private String date;        // yyyy-MM-dd
    private String time;        // HH:mm:ss
    private String description; // Transaction purpose or label
    private String vendor;      // Vendor or payer/payee
    private double amount;      // Positive for deposit, negative for payment

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // --- Constructors ---

    public Transaction() {
        // Required for Gson / CSV deserialization
    }

    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = Objects.requireNonNullElse(date, LocalDate.now().toString());
        this.time = Objects.requireNonNullElse(time, LocalTime.now().withNano(0).format(TIME_FORMATTER));
        this.description = description == null ? "" : description.trim();
        this.vendor = vendor == null ? "" : vendor.trim();
        this.amount = amount;
    }

    // --- Getters / Setters ---

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description == null ? "" : description.trim(); }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor == null ? "" : vendor.trim(); }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    // --- Utility Methods ---

    /** Converts string date to LocalDate */
    public LocalDate getLocalDate() {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            Logger.warn("Invalid date format for transaction: " + date);
            return LocalDate.now();
        }
    }

    /** Converts string time to LocalTime */
    public LocalTime getLocalTime() {
        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (Exception e) {
            Logger.warn("Invalid time format for transaction: " + time);
            return LocalTime.now();
        }
    }

    /** Checks if this transaction is a deposit (amount > 0) */
    public boolean isDeposit() {
        return amount > 0;
    }

    /** Checks if this transaction is a payment (amount < 0) */
    public boolean isPayment() {
        return amount < 0;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %-20s | %-15s | %10.2f",
                date, time, description, vendor, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(description, that.description) &&
                Objects.equals(vendor, that.vendor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, description, vendor, amount);
    }
}
