package com.pluralsight.ledger;

import com.pluralsight.models.Transaction;
import java.util.List;

/**
 * CLI reporting utility for transactions.
 */
public class Reports {

    public static void printLedger(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n==== Ledger Report ====");
        System.out.printf("%-12s | %-8s | %-20s | %-15s | %10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------------------");

        double total = 0.0;
        for (Transaction t : transactions) {
            System.out.println(t);
            total += t.getAmount();
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("Total Balance: %,.2f%n", total);
    }
}