package com.pluralsight.api;

import com.pluralsight.models.Transaction;
import com.pluralsight.services.LedgerService; // assuming you refactor Ledger into a service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoint for ledger operations.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return ledgerService.getTransactions();
    }

    @PostMapping
    public Transaction add(@RequestBody Transaction t) {
        return ledgerService.add(t);
    }

    @GetMapping("/deposits")
    public List<Transaction> deposits() {
        return ledgerService.getDeposits();
    }

    @GetMapping("/payments")
    public List<Transaction> payments() {
        return ledgerService.getPayments();
    }
}