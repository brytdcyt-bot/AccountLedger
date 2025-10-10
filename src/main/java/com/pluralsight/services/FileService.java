package com.pluralsight.services;

import com.pluralsight.models.Transaction;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class FileService {
    private static final String FILE_PATH = "transactions.csv";

    public static List<Transaction> readTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\\|");
                Transaction tx = new Transaction(
                        LocalDate.parse(parts[0]),
                        LocalTime.parse(parts[1]),
                        parts[2],
                        parts[3],
                        Double.parseDouble(parts[4])
                );
                transactions.add(tx);
            }
        } catch (IOException e) {
            System.out.println("No transaction file found. Creating a new one...");
        }
        return transactions;
    }

    public static void writeTransaction(Transaction tx) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(tx.toCSV() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

