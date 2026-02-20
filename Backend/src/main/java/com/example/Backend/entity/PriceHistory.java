package com.example.Backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Entity class for the price_history table.
// Maps to the price_history table in the database.
// Uses JPA annotations to map the class to the table.
// Uses Lombok to generate getters and setters.

@Entity
@Table(name = "price_history")
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal open;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal high;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal low;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal close;

    @Column(nullable = false, precision = 20, scale = 6)
    private BigDecimal volume;

    public PriceHistory() {} // No-arg constructor for JPA

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }

    public BigDecimal getOpen() { return open; }
    public void setOpen(BigDecimal open) { this.open = open; }

    public BigDecimal getHigh() { return high; }
    public void setHigh(BigDecimal high) { this.high = high; }

    public BigDecimal getLow() { return low; }
    public void setLow(BigDecimal low) { this.low = low; }

    public BigDecimal getClose() { return close; }
    public void setClose(BigDecimal close) { this.close = close; }

    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }
}
