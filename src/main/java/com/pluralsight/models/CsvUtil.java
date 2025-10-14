package com.pluralsight.models;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    private static final String CSV_FILE = "src/main/resources/private/transactions.csv";
    private static final String DELIMITER = "\\|";
    private static final String WRITE_DELIMITER = "|";

    /** Read all transactions from CSV */
    public static List<Transaction> readTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Path path = Paths.get(CSV_FILE);

        if (!Files.exists(path)) {
            Logger.log("No existing transaction file found. Starting with an empty ledger.");
            return transactions;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] parts = line.split(DELIMITER, -1);

                if (parts.length != 5) {
                    Logger.log("Skipping malformed line " + lineNum + ": " + line);
                    continue;
                }

                try {
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(parts[0], parts[1], parts[2], parts[3], amount));
                } catch (NumberFormatException e) {
                    Logger.log("Skipping invalid amount on line " + lineNum + ": " + parts[4]);
                }
            }

        } catch (IOException e) {
            Logger.log("Error reading CSV: " + e.getMessage());
        }

        Logger.log("Loaded " + transactions.size() + " transactions from CSV.");
        return transactions;
    }

    /** Write all transactions to CSV */
    public static void writeTransactions(List<Transaction> transactions) {
        Path path = Paths.get(CSV_FILE);

        try {
            // Ensure directories exist
            Files.createDirectories(path.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Transaction t : transactions) {
                    writer.write(String.join(WRITE_DELIMITER,
                            t.getDate(),
                            t.getTime(),
                            sanitize(t.getDescription()),
                            sanitize(t.getVendor()),
                            String.valueOf(t.getAmount())));
                    writer.newLine();
                }
            }

            Logger.log("Transactions saved successfully (" + transactions.size() + " entries).");

        } catch (IOException e) {
            Logger.log("Error writing CSV: " + e.getMessage());
        }
    }

    /** Removes potential delimiter characters to protect file structure */
    private static String sanitize(String input) {
        return input == null ? "" : input.replace(WRITE_DELIMITER, " ");
    }
}