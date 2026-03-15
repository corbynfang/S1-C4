package com.example.Backend.dto;

import com.example.Backend.entity.Account;
import com.example.Backend.entity.RiskTolerance;

import java.math.BigDecimal;
import java.time.Instant;

// Safe response DTO for auth endpoints. Excludes passwordHash so it is never sent in JSON.
public class AccountResponse {

    private Long id;
    private String username;
    private String displayName;
    private String email;
    private BigDecimal initialCash;
    private RiskTolerance riskTolerance;
    private Instant createdAt;

    public AccountResponse() {
    }

    public static AccountResponse fromAccount(Account account) {
        AccountResponse r = new AccountResponse();
        r.setId(account.getId());
        r.setUsername(account.getUsername());
        r.setDisplayName(account.getDisplayName());
        r.setEmail(account.getEmail());
        r.setInitialCash(account.getInitialCash());
        r.setRiskTolerance(account.getRiskTolerance());
        r.setCreatedAt(account.getCreatedAt());
        return r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(BigDecimal initialCash) {
        this.initialCash = initialCash;
    }

    public RiskTolerance getRiskTolerance() {
        return riskTolerance;
    }

    public void setRiskTolerance(RiskTolerance riskTolerance) {
        this.riskTolerance = riskTolerance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
