package com.example.Backend.entity;

// Risk tolerance for paper trading accounts. Stored as STRING in DB via @Enumerated(EnumType.STRING).
public enum RiskTolerance {
    LOW,
    MODERATE,
    HIGH,
    AGGRESSIVE
}
