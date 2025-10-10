package com.pluralsight;

import com.pluralsight.models.Transaction;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private static final String FILE_PATH = "transactions.csv";
    private static final String DELIMITER = "\\|";

    public static List<Transaction> readTransactions() {
        List<Transaction> list = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        try {
            if (!Files.exists(path)) Files.createFile(path);

            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split(DELIMITER);
                    if (p.length == 5) {
                        list.add(new Transaction(p[0], p[1], p[2], p[3], Double.parseDouble(p[4])));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("CSV read error: " + e.getMessage());
        }
        return list;
    }

    public static void writeTransactions(List<Transaction> transactions) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Transaction t : transactions) {
                writer.write(String.format("%s|%s|%s|%s|%.2f%n",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount()));
            }
        } catch (IOException e) {
            System.err.println("CSV write error: " + e.getMessage());
        }
    }
}