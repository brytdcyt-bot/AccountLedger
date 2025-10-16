package com.pluralsight.ledger;

import com.pluralsight.api.Ledger;
import com.pluralsight.models.Transaction;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Accounting Ledger Console UI
 * ------------------------------------------------------------
 * Provides CLI-based interaction with the Ledger backend.
 * Designed for testing core functionality before GUI launch.
 */
public class Main {

    public static void main(String[] args) {
        Ledger ledger = new Ledger(); // Assuming Ledger has a default constructor or loads from CSV
        run(ledger);
    }

    public static void run(Ledger ledger) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        Logger.log("Console dashboard started.");

        while (running) {
            printMenu();
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addTransaction(ledger, sc);
                case "2" -> Reports.printLedger(ledger.getAll());
                case "3" -> Reports.printLedger(ledger.getDeposits());
                case "4" -> Reports.printLedger(ledger.getPayments());
                case "5" -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
        Logger.log("Console dashboard closed.");
        System.out.println("\nExiting Accounting Ledger Console. Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n==== Accounting Ledger Dashboard ====");
        System.out.println("1. Add Transaction");
        System.out.println("2. View Full Ledger");
        System.out.println("3. View Deposits");
        System.out.println("4. View Payments");
        System.out.println("5. Exit");
    }

    private static void addTransaction(Ledger ledger, Scanner sc) {
        try {
            System.out.print("Description: ");
            String desc = sc.nextLine().trim();

            System.out.print("Vendor: ");
            String vendor = sc.nextLine().trim();

            System.out.print("Amount (+ for deposit, - for payment): ");
            double amt = Double.parseDouble(sc.nextLine().trim());

            Transaction t = new Transaction(desc, vendor, amt);
            ledger.add(t);
            Logger.log("Added transaction: " + t);

            System.out.println("✅ Transaction added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid amount. Transaction aborted.");
        } catch (InputMismatchException e) {
            System.out.println("⚠ Input error. Try again.");
            sc.nextLine(); // Clear buffer
        }
    }
}
