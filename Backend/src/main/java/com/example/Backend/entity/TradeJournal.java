package com.example.Backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

// Entity for the trade_journal table. Represents a single trade (buy/sell) with entry/exit details, thesis, discipline metrics, and reflection notes.

@Entity
@Table(name = "trade_journal")
public class TradeJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false, length = 4)
    private TradeType tradeType;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal shares;

    @Column(name = "entry_price", nullable = false, precision = 15, scale = 6)
    private BigDecimal entryPrice;

    @Column(name = "exit_price", precision = 15, scale = 6)
    private BigDecimal exitPrice;

    @Column(name = "stop_loss", precision = 15, scale = 6)
    private BigDecimal stopLoss;

    @Column(name = "target_price", precision = 15, scale = 6)
    private BigDecimal targetPrice;

    @Column(length = 2000)
    private String thesis;

    @Column(length = 2000)
    private String notes;

    @Column(name = "strategy_tag", length = 50)
    private String strategyTag;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Column(length = 20)
    private String result;

    @Column(name = "discipline_score", precision = 5, scale = 2)
    private BigDecimal disciplineScore;

    @Column(length = 500)
    private String violations;

    public enum TradeType {
        BUY,
        SELL
    }

    public TradeJournal() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public TradeType getTradeType() { return tradeType; }
    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }

    public BigDecimal getShares() { return shares; }
    public void setShares(BigDecimal shares) { this.shares = shares; }

    public BigDecimal getEntryPrice() { return entryPrice; }
    public void setEntryPrice(BigDecimal entryPrice) { this.entryPrice = entryPrice; }

    public BigDecimal getExitPrice() { return exitPrice; }
    public void setExitPrice(BigDecimal exitPrice) { this.exitPrice = exitPrice; }

    public BigDecimal getStopLoss() { return stopLoss; }
    public void setStopLoss(BigDecimal stopLoss) { this.stopLoss = stopLoss; }

    public BigDecimal getTargetPrice() { return targetPrice; }
    public void setTargetPrice(BigDecimal targetPrice) { this.targetPrice = targetPrice; }

    public String getThesis() { return thesis; }
    public void setThesis(String thesis) { this.thesis = thesis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStrategyTag() { return strategyTag; }
    public void setStrategyTag(String strategyTag) { this.strategyTag = strategyTag; }

    public LocalDate getTradeDate() { return tradeDate; }
    public void setTradeDate(LocalDate tradeDate) { this.tradeDate = tradeDate; }

    public LocalDate getCloseDate() { return closeDate; }
    public void setCloseDate(LocalDate closeDate) { this.closeDate = closeDate; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public BigDecimal getDisciplineScore() { return disciplineScore; }
    public void setDisciplineScore(BigDecimal disciplineScore) { this.disciplineScore = disciplineScore; }

    public String getViolations() { return violations; }
    public void setViolations(String violations) { this.violations = violations; }
}
