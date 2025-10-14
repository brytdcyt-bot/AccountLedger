import java.util.List;

public class Reports {

    public static double calculateTotal(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public static void printLedger(List<Transaction> transactions) {
        System.out.println("Date       | Time     | Description      | Vendor       | Amount");
        System.out.println("-----------------------------------------------------------------");
        for (Transaction tx : transactions) {
            System.out.printf("%s | %s | %-15s | %-12s | %8.2f%n",
                    tx.getDate(), tx.getTime(), tx.getDescription(), tx.getVendor(), tx.getAmount());
        }
        System.out.printf("%nTotal Balance: %.2f%n", calculateTotal(transactions));
    }
}