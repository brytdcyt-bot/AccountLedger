import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String CSV_FILE = "transactions.csv";

    public static void main(String[] args) {
        List<Transaction> transactions;
        try {
            transactions = CsvUtil.loadTransactions(CSV_FILE);
        } catch (IOException e) {
            Logger.log("Failed to load transactions: " + e.getMessage());
            transactions = List.of();
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nLedger Dashboard");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Ledger");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Description: ");
                    String desc = scanner.nextLine();
                    System.out.print("Vendor: ");
                    String vendor = scanner.nextLine();
                    System.out.print("Amount: ");
                    double amount = Double.parseDouble(scanner.nextLine());

                    Transaction tx = new Transaction(desc, vendor, amount);
                    transactions.add(tx);
                    try {
                        CsvUtil.saveTransactions(CSV_FILE, transactions);
                        Logger.log("Added transaction: " + tx);
                    } catch (IOException e) {
                        Logger.log("Failed to save transaction: " + e.getMessage());
                    }
                    break;

                case "2":
                    Reports.printLedger(transactions);
                    break;

                case "3":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}