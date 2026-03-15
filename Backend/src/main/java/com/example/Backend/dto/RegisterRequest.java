package com.example.Backend.dto;

import com.example.Backend.entity.RiskTolerance;

// Request body for POST /api/auth/register. Never expose Account entity directly.
public class RegisterRequest {

    private String username;
    private String password;
    private String displayName;
    private RiskTolerance riskTolerance;

    public RegisterRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RiskTolerance getRiskTolerance() {
        return riskTolerance;
    }

    public void setRiskTolerance(RiskTolerance riskTolerance) {
        this.riskTolerance = riskTolerance;
    }
}
