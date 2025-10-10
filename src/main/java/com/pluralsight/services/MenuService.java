package com.pluralsight.services;

import com.pluralsight.Ledger;
import com.pluralsight.Reports;
import com.pluralsight.models.Transaction;

import java.time.*;
import java.util.*;

public class MenuService {
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Transaction> transactions = FileService.readTransactions();
    private static Ledger ledger = new Ledger(transactions);

    public static void start() {
        while (true) {
            System.out.println("\n=== HOME MENU ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D" -> addTransaction(true);
                case "P" -> addTransaction(false);
                case "L" -> ledgerMenu();
                case "X" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addTransaction(boolean isDeposit) {
        try {
            System.out.print("Enter description: ");
            String desc = scanner.nextLine();
            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (!isDeposit) amount = -Math.abs(amount);

            Transaction tx = new Transaction(LocalDate.now(), LocalTime.now(), desc, vendor, amount);
            FileService.writeTransaction(tx);
            transactions.add(tx);

            System.out.println("Transaction saved successfully!");
        } catch (Exception e) {
            System.out.println("Error adding transaction.");
        }
    }

    private static void ledgerMenu() {
        while (true) {
            System.out.println("\n=== LEDGER MENU ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim().toUpperCase();

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

    private static void reportsMenu() {
        while (true) {
            System.out.println("\n=== REPORTS MENU ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> Reports.monthToDate(transactions);
                case "2" -> Reports.previousMonth(transactions);
                case "3" -> Reports.yearToDate(transactions);
                case "4" -> Reports.previousYear(transactions);
                case "5" -> {
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine();
                    Reports.searchByVendor(transactions, vendor);
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}

