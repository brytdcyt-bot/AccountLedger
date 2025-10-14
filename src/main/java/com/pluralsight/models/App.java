package com.pluralsight.models;

public class App {

    public static void main(String[] args) {
        System.out.println("=== Ledger System Boot ===");

        String mode = (args.length > 0) ? args[0].trim().toLowerCase() : "cli";

        switch (mode) {
            case "web" -> {
                System.out.println("→ Starting in Web Mode...");
                WebServer.main(args);
            }
            case "cli" -> {
                System.out.println("→ Starting in CLI Mode...");
                Main.main(args);
            }
            default -> {
                System.out.printf("Unknown mode '%s'. Defaulting to CLI.%n", mode);
                Main.main(args);
            }
        }
    }
}
