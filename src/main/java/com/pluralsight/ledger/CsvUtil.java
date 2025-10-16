package com.pluralsight.ledger;

import com.pluralsight.models.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV read/write helper for ledger transactions.
 */
public class CsvUtil {
    private static final String DEFAULT_FILE = "ledger.csv";

    public static List<Transaction> readTransactions(String path) {
        List<Transaction> transactions = new ArrayList<>();
        Path filePath = Paths.get(path == null ? DEFAULT_FILE : path);

        if (!Files.exists(filePath)) return transactions;

        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            br.lines()
                    .skip(1)
                    .map(Transaction::fromCsv)
                    .filter(t -> t != null)
                    .forEach(transactions::add);
        } catch (IOException e) {
            Logger.log("Error reading CSV: " + e.getMessage());
        }
        return transactions;
    }

    public static void writeTransactions(String path, List<Transaction> transactions) {
        Path filePath = Paths.get(path == null ? DEFAULT_FILE : path);

        try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
            bw.write("Date,Time,Description,Vendor,Amount\n");
            for (Transaction t : transactions)
                bw.write(t.toCsv() + "\n");
        } catch (IOException e) {
            Logger.log("Error writing CSV: " + e.getMessage());
        }
    }

    public static void append(String path, Transaction t) {
        Path filePath = Paths.get(path == null ? DEFAULT_FILE : path);
        boolean exists = Files.exists(filePath);

        try (BufferedWriter bw = Files.newBufferedWriter(filePath,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (!exists)
                bw.write("Date,Time,Description,Vendor,Amount\n");
            bw.write(t.toCsv() + "\n");
        } catch (IOException e) {
            Logger.log("Error appending to CSV: " + e.getMessage());
        }
    }
}