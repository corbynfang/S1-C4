package com.example.Backend.dto;

import java.math.BigDecimal;
import java.util.List;

// DTO for behavioral stats returned by GET /api/trades/summary. Aggregates discipline metrics and trade counts.
public class TradeSummary {

    private int totalTrades;
    private BigDecimal averageDisciplineScore;
    private int winCount;
    private int lossCount;
    private List<String> topViolations;

    public TradeSummary() {
    }

    public TradeSummary(int totalTrades, BigDecimal averageDisciplineScore,
                        int winCount, int lossCount, List<String> topViolations) {
        this.totalTrades = totalTrades;
        this.averageDisciplineScore = averageDisciplineScore;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.topViolations = topViolations;
    }

    public int getTotalTrades() { return totalTrades; }
    public void setTotalTrades(int totalTrades) { this.totalTrades = totalTrades; }

    public BigDecimal getAverageDisciplineScore() { return averageDisciplineScore; }
    public void setAverageDisciplineScore(BigDecimal averageDisciplineScore) { this.averageDisciplineScore = averageDisciplineScore; }

    public int getWinCount() { return winCount; }
    public void setWinCount(int winCount) { this.winCount = winCount; }

    public int getLossCount() { return lossCount; }
    public void setLossCount(int lossCount) { this.lossCount = lossCount; }

    public List<String> getTopViolations() { return topViolations; }
    public void setTopViolations(List<String> topViolations) { this.topViolations = topViolations; }
}
