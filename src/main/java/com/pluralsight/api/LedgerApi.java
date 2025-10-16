package com.pluralsight.api;

import com.pluralsight.models.Transaction;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoint for ledger operations.
 * Can coexist with TransactionController.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ledger")
public class LedgerApi {

    private final Ledger ledger = new Ledger();

    @GetMapping
    public List<Transaction> getAll() {
        return ledger.getAll();
    }

    @PostMapping
    public Transaction add(@RequestBody Transaction t) {
        return ledger.add(t);
    }

    @GetMapping("/deposits")
    public List<Transaction> deposits() {
        return ledger.getDeposits();
    }

    @GetMapping("/payments")
    public List<Transaction> payments() {
        return ledger.getPayments();
    }
}