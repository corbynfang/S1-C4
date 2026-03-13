package com.example.Backend.controller;

import com.example.Backend.dto.TradeSummary;
import com.example.Backend.entity.Account;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller for account endpoints. Handles CRUD for student accounts and progress/trades per account.
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account saved = accountService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}/trades")
    public ResponseEntity<List<TradeJournal>> getAccountTrades(@PathVariable Long id) {
        if (accountService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountService.getTradesForAccount(id));
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<TradeSummary> getAccountProgress(@PathVariable Long id) {
        if (accountService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountService.getProgressForAccount(id));
    }
}
