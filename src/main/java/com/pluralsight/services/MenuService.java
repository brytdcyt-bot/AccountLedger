package com.pluralsight.services;

import com.pluralsight.models.Ledger;
import com.pluralsight.models.Logger;
import com.pluralsight.models.Reports;
import com.pluralsight.models.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Handles all user interaction via the console (CLI mode).
 * Provides navigation for deposits, payments, and reports.
 */
public class MenuService {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Transaction> transactions = FileService.readTransactions();
    private static final Ledger ledger = new Ledger(transactions);

    /** Entry point for the main menu loop. */
    public static void start() {
        Logger.info("Launching CLI Menu...");

        while (true) {
            printMainMenu();
            String choice = prompt("Choose an option: ").toUpperCase();

            switch (choice) {
                case "D" -> addTransaction(true);
                case "P" -> addTransaction(false);
                case "L" -> ledgerMenu();
                case "X" -> {
                    Logger.info("Exiting CLI Menu. Goodbye!");
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    /** Displays the main menu. */
    private static void printMainMenu() {
        System.out.println("\n=== HOME MENU ===");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
    }

    /** Prompts user for transaction info and saves it. */
    private static void addTransaction(boolean isDeposit) {
        try {
            String desc = prompt("Enter description: ");
            String vendor = prompt("Enter vendor: ");
            double amount = Double.parseDouble(prompt("Enter amount: "));

            if (!isDeposit) amount = -Math.abs(amount);

            Transaction tx = new Transaction(
                    LocalDate.now().toString(),
                    LocalTime.now().toString(),
                    desc,
                    vendor,
                    amount
            );

            FileService.writeTransaction(tx);
            transactions.add(tx);
            Logger.info("Transaction added: " + tx);
            System.out.println("✅ Transaction saved successfully!");

        } catch (NumberFormatException e) {
            Logger.warn("Invalid amount entered.");
            System.out.println("❌ Invalid amount. Please enter a number.");
        } catch (Exception e) {
            Logger.error("Error adding transaction: " + e.getMessage());
            System.out.println("❌ An error occurred while adding the transaction.");
        }
    }

    /** Submenu for ledger display and filters. */
    private static void ledgerMenu() {
        while (true) {
            printLedgerMenu();
            String choice = prompt("Choose an option: ").toUpperCase();

            switch (choice) {
                case "A" -> ledger.displayAll();
                case "D" -> ledger.displayDeposits();
                case "P" -> ledger.displayPayments();
                case "R" -> reportsMenu();
                case "H" -> { return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    /** Displays the ledger submenu. */
    private static void printLedgerMenu() {
        System.out.println("\n=== LEDGER MENU ===");
        System.out.println("A) All Transactions");
        System.out.println("D) Deposits Only");
        System.out.println("P) Payments Only");
        System.out.println("R) Reports");
        System.out.println("H) Home");
    }

    /** Reports submenu for period-based summaries and searches. */
    private static void reportsMenu() {
        while (true) {
            printReportsMenu();
            String choice = prompt("Choose an option: ");

            switch (choice) {
                case "1" -> Reports.monthToDate(transactions);
                case "2" -> Reports.previousMonth(transactions);
                case "3" -> Reports.yearToDate(transactions);
                case "4" -> Reports.previousYear(transactions);
                case "5" -> {
                    String vendor = prompt("Enter vendor name: ");
                    Reports.searchByVendor(transactions, vendor);
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    /** Displays the reports submenu. */
    private static void printReportsMenu() {
        System.out.println("\n=== REPORTS MENU ===");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");
    }

    /** Helper for safe input. */
    private static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}