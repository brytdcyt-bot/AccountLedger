package com.pluralsight.services;

import com.pluralsight.models.Logger;
import com.pluralsight.models.Transaction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing Transaction data from/to CSV files.
 * Uses a simple pipe-delimited format for persistence.
 */
public class FileService {

    private static final Path FILE_PATH = Paths.get("src/main/resources/private/transactions.csv");
    private static final String DELIMITER = "\\|";

    static {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
                Logger.info("Created new transaction file: " + FILE_PATH);
            }
        } catch (IOException e) {
            Logger.error("Failed to initialize transaction file: " + e.getMessage());
        }
    }

    /** Read all transactions from file (gracefully skips invalid lines). */
    public static List<Transaction> readTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        if (!Files.exists(FILE_PATH)) {
            Logger.warn("Transaction file not found. Returning empty list.");
            return transactions;
        }

        try {
            List<String> lines = Files.readAllLines(FILE_PATH);

            for (String line : lines) {
                if (line.isBlank()) continue;

                String[] parts = line.split(DELIMITER, -1);
                if (parts.length != 5) {
                    Logger.warn("Skipping malformed line: " + line);
                    continue;
                }

                try {
                    Transaction tx = new Transaction(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            Double.parseDouble(parts[4])
                    );
                    transactions.add(tx);
                } catch (Exception e) {
                    Logger.warn("Error parsing transaction line: " + line + " (" + e.getMessage() + ")");
                }
            }

            Logger.info("Loaded " + transactions.size() + " transactions from CSV.");
        } catch (IOException e) {
            Logger.error("Error reading transactions: " + e.getMessage());
        }

        return transactions;
    }

    /** Append a single transaction to the file. */
    public static void writeTransaction(Transaction tx) {
        if (tx == null) {
            Logger.warn("Attempted to write null transaction — ignored.");
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH, StandardOpenOption.APPEND)) {
            writer.write(String.join("|",
                    tx.getDate(),
                    tx.getTime(),
                    tx.getDescription(),
                    tx.getVendor(),
                    String.valueOf(tx.getAmount())
            ));
            writer.newLine();
            Logger.info("Transaction written: " + tx);
        } catch (IOException e) {
            Logger.error("Failed to write transaction: " + e.getMessage());
        }
    }

    /** Overwrite the entire file with a new list of transactions. */
    public static void writeAll(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            Logger.warn("Attempted to write empty transaction list — file unchanged.");
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Transaction tx : transactions) {
                writer.write(String.join("|",
                        tx.getDate(),
                        tx.getTime(),
                        tx.getDescription(),
                        tx.getVendor(),
                        String.valueOf(tx.getAmount())
                ));
                writer.newLine();
            }
            Logger.info("Overwrote transaction file with " + transactions.size() + " entries.");
        } catch (IOException e) {
            Logger.error("Error overwriting transactions: " + e.getMessage());
        }
    }
}