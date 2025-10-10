package com.pluralsight;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Ledger System Boot ===");

        if (args.length > 0 && args[0].equalsIgnoreCase("web")) {
            System.out.println("Starting in Web Mode...");
            WebServer.main(args);
        } else {
            System.out.println("Starting in CLI Mode...");
            Main.main(args);
        }
    }
}