public class Transaction {
    private final String date; // YYYY-MM-DD
    private final String description;
    private final double amount;
    private final String vendor;

    public Transaction(String date, String description, double amount, String vendor) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.vendor = vendor;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getVendor() {
        return vendor;
    }

    @Override
    public String toString() {
        return date + "|"  + description + "|" + amount + "|" + vendor;
    }
}