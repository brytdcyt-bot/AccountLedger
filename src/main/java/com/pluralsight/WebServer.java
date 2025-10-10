package com.pluralsight;

import com.google.gson.Gson;
import com.pluralsight.models.Transaction;
import spark.Filter;

import java.util.List;

import static spark.Spark.*;

public class WebServer {
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        // === Server configuration ===
        port(8080);
        staticFiles.location("/public"); // serves src/main/resources/public

        enableCORS("*", "GET,POST,OPTIONS", "Content-Type,Authorization");

        Ledger ledger = new Ledger(CsvUtil.readTransactions());
        Reports reports = new Reports(ledger);

        // === API Endpoints ===

        // Health check
        get("/api/health", (req, res) -> "OK");

        // Get all transactions
        get("/api/transactions", (req, res) -> {
            res.type("application/json");
            List<Transaction> all = ledger.getAll();
            return gson.toJson(all);
        });

        // Add a new transaction
        post("/api/transactions", (req, res) -> {
            res.type("application/json");
            Transaction t = gson.fromJson(req.body(), Transaction.class);
            if (t.getDescription() == null || t.getVendor() == null) {
                res.status(400);
                return gson.toJson(new ErrorResponse("Invalid transaction data"));
            }
            ledger.add(t);
            res.status(201);
            return gson.toJson(t);
        });

        // Get reports by type
        get("/api/reports/:type", (req, res) -> {
            res.type("application/json");
            String type = req.params(":type");
            List<Transaction> data = switch (type.toLowerCase()) {
                case "month-to-date" -> reports.monthToDate();
                case "previous-month" -> reports.previousMonth();
                case "year-to-date" -> reports.yearToDate();
                case "previous-year" -> reports.previousYear();
                default -> List.of();
            };
            return gson.toJson(data);
        });

        // Catch-all for invalid routes
        notFound((req, res) -> {
            res.type("application/json");
            return gson.toJson(new ErrorResponse("Route not found: " + req.pathInfo()));
        });

        System.out.println("🌐 Web server running at http://localhost:8080");
    }

    // === Utility for JSON error responses ===
    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String message) {
            this.error = message;
        }

        public String getError() {
            return error;
        }
    }

    // === Enable CORS for local testing ===
    private static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Allow-Methods", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }
}
