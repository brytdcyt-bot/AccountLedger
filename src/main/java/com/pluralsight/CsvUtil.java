import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    private static final String CSV_HEADER = "Date,Time,Description,Vendor,Amount";

    public static void saveTransactions(String filePath, List<Transaction> transactions) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Transaction tx : transactions) {
                writer.write(tx.toString());
                writer.newLine();
            }
        }
    }

    public static List<Transaction> loadTransactions(String filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return transactions;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;

                LocalDateTime timestamp = LocalDateTime.parse(
                        parts[0] + " " + parts[1],
                        DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")
                );

                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                transactions.add(new Transaction(timestamp, description, vendor, amount));
            }
        }

        return transactions;
    }
}
