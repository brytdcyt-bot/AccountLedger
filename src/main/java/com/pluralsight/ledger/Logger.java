package com.pluralsight.ledger;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple file logger for debugging and audit purposes.
 */
public class Logger {
    private static final Path LOG_PATH = Paths.get("logs", "ledger.log");

    static {
        try {
            Files.createDirectories(LOG_PATH.getParent());
        } catch (IOException e) {
            System.err.println("⚠ Failed to create log directory: " + e.getMessage());
        }
    }

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = String.format("[%s] %s%n", timestamp, message);

        try {
            Files.writeString(LOG_PATH, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("⚠ Logging failed: " + e.getMessage());
        }
    }
}
