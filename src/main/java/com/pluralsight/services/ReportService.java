package com.pluralsight.services;

import com.pluralsight.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {

    public List<Transaction> filterByDateRange(List<Transaction> all, LocalDate start, LocalDate end) {
        return all.stream()
                .filter(t -> {
                    LocalDate date = t.getDateTime().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByVendor(List<Transaction> all, String vendor) {
        return all.stream()
                .filter(t -> t.getVendor().equalsIgnoreCase(vendor))
                .collect(Collectors.toList());
    }
}

