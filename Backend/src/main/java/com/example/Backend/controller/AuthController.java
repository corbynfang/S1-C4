package com.example.Backend.controller;

import com.example.Backend.dto.AccountResponse;
import com.example.Backend.dto.LoginRequest;
import com.example.Backend.dto.RegisterRequest;
import com.example.Backend.service.AccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST controller for auth: register and login. Returns AccountResponse (no passwordHash).
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null || request.getDisplayName() == null) {
            return ResponseEntity.badRequest().body("username, password, and displayName are required");
        }
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(AccountResponse.fromAccount(
                            accountService.registerAccount(
                                    request.getUsername(),
                                    request.getPassword(),
                                    request.getDisplayName(),
                                    request.getRiskTolerance()
                            )
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("username and password are required");
        }
        return accountService.authenticateUser(request.getUsername(), request.getPassword())
                .map(AccountResponse::fromAccount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
