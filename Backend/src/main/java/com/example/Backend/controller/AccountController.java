package com.example.Backend.controller;

import com.example.Backend.dto.AccountResponse;
import com.example.Backend.dto.TradeSummary;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// REST controller for account endpoints. Returns AccountResponse (no passwordHash). Account creation is via POST /api/auth/register.
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> responses = accountService.findAll().stream()
                .map(AccountResponse::fromAccount)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        return accountService.findById(id)
                .map(AccountResponse::fromAccount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
