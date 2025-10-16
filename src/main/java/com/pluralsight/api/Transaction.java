package com.pluralsight.api;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction() {} // Needed for JSON deserialization

    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = String.valueOf(LocalDate.parse(date)); }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = String.valueOf(LocalTime.parse(time)); }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}


