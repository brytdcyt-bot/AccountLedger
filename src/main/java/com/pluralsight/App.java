package com.pluralsight;

import com.sun.tools.javac.Main;

import java.io.IOException;
import java.util.List;

public class App {
    public static <Transaction> void main(String[] args) {
        final String CSV_FILE = "transactions.csv";

        try {
            CsvUtil CsvUtil;
            List<Transaction> transactions = CsvUtil.loadTransactions(CSV_FILE);

            // Start web server
            WebServer webServer = new WebServer();
            webServer.start(8080, transactions);

            // Launch console dashboard
            Main.main(args);

        } catch (IOException e) {
            Logger.log("Failed to start app: " + e.getMessage());
        }
    }
}

