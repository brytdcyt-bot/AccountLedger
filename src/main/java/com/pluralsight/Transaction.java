import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private LocalDateTime timestamp;
    private String description;
    private String vendor;
    private double amount;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a");

    public Transaction(String description, String vendor, double amount) {
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public Transaction(LocalDateTime timestamp, String description, String vendor, double amount) {
        this.timestamp = timestamp;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String getDate() {
        return timestamp.format(DATE_FORMAT);
    }

    public String getTime() {
        return timestamp.format(TIME_FORMAT);
    }

    public String getDescription() { return description; }

    public String getVendor() { return vendor; }

    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%.2f", getDate(), getTime(), description, vendor, amount);
    }
}
