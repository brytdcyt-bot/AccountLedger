package com.pluralsight.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Accounting Ledger REST Application
 * -------------------------------------------
 * Bootstraps the Spring environment and loads
 * API controllers, models, and service layers.
 */
@SpringBootApplication(scanBasePackages = "com.pluralsight")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("✅ Accounting Ledger API running on http://localhost:8080");
    }
}
