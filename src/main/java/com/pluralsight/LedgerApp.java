package com.pluralsight;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

// -------------------- Transaction --------------------
class Transaction {
    private final String date;
    private final String time;
    private final String description;
    private final String vendor;
    private final double amount;

    public Transaction(String description, String vendor, double amount) {
        LocalDateTime now = LocalDateTime.now();
        this.date = now.toLocalDate().toString();
        this.time = now.toLocalTime().withNano(0).toString();
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return date + "," + time + "," + description + "," + vendor + "," + amount;
    }
}

// -------------------- Logger --------------------
class Logger {
    private static final String LOG_FILE = "ledger.log";

    public static void log(String message) {
        String entry = "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message;
        System.out.println(entry);
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(entry + "\n");
        } catch (IOException ignored) {}
    }
}

// -------------------- CsvUtil --------------------
class CsvUtil {
    private static final String CSV_FILE = "transactions.csv";

    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(CSV_FILE);
        if (!file.exists()) return transactions;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;
                transactions.add(new Transaction(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4])));
            }
        } catch (IOException e) { Logger.log("CSV load failed: " + e.getMessage()); }
        return transactions;
    }

    public static void saveTransactions(List<Transaction> transactions) {
        try (FileWriter fw = new FileWriter(CSV_FILE)) {
            for (Transaction t : transactions) fw.write(t.toString() + "\n");
        } catch (IOException e) { Logger.log("CSV save failed: " + e.getMessage()); }
    }
}

// -------------------- Ledger --------------------
class Ledger {
    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    public Ledger() { transactions.addAll(CsvUtil.loadTransactions()); }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        CsvUtil.saveTransactions(transactions);
        Logger.log("Transaction added: " + t);
    }

    public List<Transaction> getAll() { return new ArrayList<>(transactions); }
    public List<Transaction> getDeposits() {
        List<Transaction> res = new ArrayList<>();
        for (Transaction t : transactions) if (t.getAmount() > 0) res.add(t);
        return res;
    }
    public List<Transaction> getPayments() {
        List<Transaction> res = new ArrayList<>();
        for (Transaction t : transactions) if (t.getAmount() < 0) res.add(t);
        return res;
    }
    public List<Transaction> getByVendor(String vendor) {
        List<Transaction> res = new ArrayList<>();
        for (Transaction t : transactions)
            if (t.getVendor().equalsIgnoreCase(vendor)) res.add(t);
        return res;
    }
}

// -------------------- WebServer --------------------
class WebServer {
    private final Ledger ledger;
    private HttpServer server;

    public WebServer(Ledger ledger) { this.ledger = ledger; }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        // GET all transactions
        server.createContext("/api/transactions", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1); return;
            }
            String json = "[";
            for (Transaction t : ledger.getAll()) {
                json += String.format("{\"date\":\"%s\",\"time\":\"%s\",\"description\":\"%s\",\"vendor\":\"%s\",\"amount\":%.2f},",
                        t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
            }
            if (json.endsWith(",")) json = json.substring(0, json.length() - 1);
            json += "]";
            byte[] bytes = json.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });

        server.start();
        Logger.log("Web server started at http://localhost:" + port);
    }

    public void stop() { if (server != null) server.stop(0); }
}

// -------------------- Main --------------------
public class LedgerApp {
    public static void main(String[] args) throws IOException {
        Ledger ledger = new Ledger();
        WebServer ws = new WebServer(ledger);
        ws.start(8080);

        // Simple console
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\nLedger Dashboard (Console)");
            System.out.println("1. Add Transaction  2. View Ledger  3. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Description: "); String d = sc.nextLine();
                    System.out.print("Vendor: "); String v = sc.nextLine();
                    System.out.print("Amount: "); double a = Double.parseDouble(sc.nextLine());
                    ledger.addTransaction(new Transaction(d, v, a));
                    break;
                case "2":
                    System.out.println("Date | Time | Desc | Vendor | Amount");
                    for (Transaction t : ledger.getAll())
                        System.out.printf("%s %s %s %s %.2f%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
                    break;
                case "3": running = false; break;
                default: System.out.println("Invalid choice."); break;
            }
        }
        sc.close();
        ws.stop();
        Logger.log("LedgerApp terminated.");
    }
}