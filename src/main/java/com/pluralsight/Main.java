package com.pluralsight;

import com.pluralsight.models.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Ledger ledger = new Ledger(CsvUtil.readTransactions());
        Reports reports = new Reports(ledger);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Ledger CLI ===");
            System.out.println("[1] Add Deposit");
            System.out.println("[2] Make Payment");
            System.out.println("[3] View Ledger");
            System.out.println("[4] Reports");
            System.out.println("[0] Exit");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addTransaction(scanner, ledger, true);
                case "2" -> addTransaction(scanner, ledger, false);
                case "3" -> showLedger(ledger);
                case "4" -> showReports(scanner, reports);
                case "0" -> { ledger.save(); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addTransaction(Scanner sc, Ledger ledger, boolean deposit) {
        System.out.print("Description: ");
        String desc = sc.nextLine();
        System.out.print("Vendor: ");
        String vendor = sc.nextLine();
        System.out.print("Amount: ");
        double amt = Double.parseDouble(sc.nextLine());
        if (!deposit) amt *= -1;

        Transaction tx = new Transaction(
                LocalDateTime.now().toLocalDate().toString(),
                LocalDateTime.now().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                desc, vendor, amt
        );

        ledger.add(tx);
        System.out.println("Saved.");
    }

    private static void showLedger(Ledger ledger) {
        ledger.getAll().forEach(System.out::println);
    }

    private static void showReports(Scanner sc, Reports reports) {
        System.out.println("\n[1] Month-to-date\n[2] Previous Month\n[3] Year-to-date\n[4] Previous Year\n[5] Search by Vendor\n[0] Back");
        System.out.print("> ");
        String c = sc.nextLine();

        List<Transaction> result = switch (c) {
            case "1" -> reports.monthToDate();
            case "2" -> reports.previousMonth();
            case "3" -> reports.yearToDate();
            case "4" -> reports.previousYear();
            case "5" -> {
                System.out.print("Vendor name: ");
                yield reports.searchByVendor(sc.nextLine());
            }
            default -> List.of();
        };

        result.forEach(System.out::println);
    }
}
