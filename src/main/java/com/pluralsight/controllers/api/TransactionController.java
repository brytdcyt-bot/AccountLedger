package com.pluralsight.controllers.api;

import com.pluralsight.api.Ledger;
import com.pluralsight.api.Transaction;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST controller for managing transactions.
 * Provides JSON endpoints for ledger operations.
 */
@CrossOrigin(origins = "*") // Allow requests from any frontend port
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final Ledger ledger = new Ledger();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    // GET: /api/transactions
    @GetMapping
    public List<Transaction> getAll() {
        return ledger.getAll();
    }

    // POST: /api/transactions
    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction tx) {
        LocalDateTime now = LocalDateTime.now();

        if (tx.getDate() == null)
            tx.setDate(now.format(dateFormatter));
        if (tx.getTime() == null)
            tx.setTime(now.format(timeFormatter));

        ledger.add(tx);
        return tx;
    }

    // GET: /api/transactions/deposits
    @GetMapping("/deposits")
    public List<Transaction> getDeposits() {
        return ledger.getDeposits();
    }

    // GET: /api/transactions/payments
    @GetMapping("/payments")
    public List<Transaction> getPayments() {
        return ledger.getPayments();
    }
}