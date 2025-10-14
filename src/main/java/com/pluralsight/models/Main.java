package com.pluralsight.models;

import com.pluralsight.services.TransactionService;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TransactionService service = new TransactionService();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMainMenu();
            switch (scanner.nextLine().trim()) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> showLedger();
                case "4" -> showReports();
                case "0" -> {
                    service.save();
                    System.out.println("Exiting... Transactions saved.");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n=== Ledger CLI ===");
        System.out.println("[1] Add Deposit");
        System.out.println("[2] Make Payment");
        System.out.println("[3] View Ledger");
        System.out.println("[4] Reports");
        System.out.println("[0] Exit");
        System.out.print("> ");
    }

    private static void addTransaction(boolean deposit) {
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine().trim();

        double amount = readDouble("Amount: ");

        var transaction = deposit
                ? service.addDeposit(description, vendor, amount)
                : service.addPayment(description, vendor, amount);

        System.out.println("✅ Saved: " + transaction);
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static void showLedger() {
        List<Transaction> all = service.getAllTransactions();
        if (all.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            all.forEach(System.out::println);
        }
    }

    private static void showReports() {
        System.out.println("""
                
                === Reports ===
                [1] Month-to-Date
                [2] Previous Month
                [3] Year-to-Date
                [4] Previous Year
                [5] Search by Vendor
                [0] Back
                """);
        System.out.print("> ");
        String choice = scanner.nextLine().trim();

        List<Transaction> result = switch (choice) {
            case "1" -> service.monthToDate();
            case "2" -> service.previousMonth();
            case "3" -> service.yearToDate();
            case "4" -> service.previousYear();
            case "5" -> {
                System.out.print("Vendor name: ");
                yield service.getByVendor(scanner.nextLine().trim());
            }
            default -> List.of();
        };

        if (result.isEmpty()) {
            System.out.println("No matching transactions found.");
        } else {
            result.forEach(System.out::println);
        }
    }
}
