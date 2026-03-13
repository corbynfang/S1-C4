package com.example.Backend.service;

import com.example.Backend.dto.TradeSummary;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.repository.TradeJournalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

// Service for trade journal operations. Handles logging trades, computing discipline scores, detecting violations, and providing trade summaries for behavioral data.
// WHAT IS CONSIDERED A GOOD TRADE: 
// - Has written thesis = 20 points
// - Has stop loss set = 15 points
// - Has target price set = 15 points
// - Position size under 20% of portfolio = 20 points
// - Has strategy tag = 20 points
// - Holding period at least 1 day = 10 points
// - Has written thesis = 20 points
// - Has stop loss set = 15 points
// - Has target price set = 15 points
// - Position size under 20% of portfolio = 20 points
// - Has strategy tag = 20 points
// - Holding period at least 1 day = 10 points

@Service
public class TradeJournalService {

    private static final BigDecimal PORTFOLIO_PERCENT_THRESHOLD = new BigDecimal("0.20");
    private static final int MIN_HOLDING_DAYS = 1;

    private final TradeJournalRepository repository;

    public TradeJournalService(TradeJournalRepository repository) {
        this.repository = repository;
    }

    // Log a new trade. Computes discipline score and violations using the given portfolio value, then saves the trade.
    @Transactional
    public TradeJournal logTrade(TradeJournal trade, BigDecimal portfolioValue) {
        int score = computeDisciplineScore(trade, portfolioValue);
        String violationsStr = computeViolations(trade, portfolioValue);

        trade.setDisciplineScore(BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP));
        trade.setViolations(violationsStr);

        return repository.save(trade);
    }

    // Compute discipline score (0-100) based on:
    // - Has written thesis = 20 points
    // - Has stop loss set = 15 points
    // - Has target price set = 15 points
    // - Position size under 20% of portfolio = 20 points
    // - Has strategy tag = 20 points
    // - Holding period at least 1 day = 10 points
    public int computeDisciplineScore(TradeJournal trade, BigDecimal portfolioValue) {
        int score = 0;

        if (hasWrittenThesis(trade)) score += 20;
        if (hasStopLossSet(trade)) score += 15;
        if (hasTargetPriceSet(trade)) score += 15;
        if (isPositionSizeUnderThreshold(trade, portfolioValue)) score += 20;
        if (hasStrategyTag(trade)) score += 20;
        if (hasMinimumHoldingPeriod(trade)) score += 10;

        return Math.min(score, 100);
    }
    // Detect discipline violations and return them as a comma-separated string.
    // Returns a comma-separated list of violation descriptions, or empty string if none.
    // Uses a list of violations to build a comma-separated string.
    // Returns a comma-separated list of violation descriptions, or empty string if none.
    // Uses a list of violations to build a comma-separated string.
    // Returns a comma-separated list of violation descriptions, or empty string if none.
    // Uses a list of violations to build a comma-separated string.
    public String computeViolations(TradeJournal trade, BigDecimal portfolioValue) {
        List<String> violations = new ArrayList<>();

        if (!hasWrittenThesis(trade)) violations.add("No thesis written");
        if (!hasStopLossSet(trade)) violations.add("No stop loss set");
        if (!hasTargetPriceSet(trade)) violations.add("No target price set");
        if (!isPositionSizeUnderThreshold(trade, portfolioValue)) violations.add("Position size over 20% of portfolio");
        if (!hasStrategyTag(trade)) violations.add("No strategy tag");
        if (!hasMinimumHoldingPeriod(trade)) violations.add("Holding period under 1 day");

        return String.join(", ", violations);
    }

    private boolean hasWrittenThesis(TradeJournal trade) {
        return trade.getThesis() != null && !trade.getThesis().isBlank();
    }

    private boolean hasStopLossSet(TradeJournal trade) {
        return trade.getStopLoss() != null;
    }

    private boolean hasTargetPriceSet(TradeJournal trade) {
        return trade.getTargetPrice() != null;
    }

    private boolean isPositionSizeUnderThreshold(TradeJournal trade, BigDecimal portfolioValue) {
        if (portfolioValue == null || portfolioValue.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal positionValue = trade.getEntryPrice().multiply(trade.getShares());
        BigDecimal percent = positionValue.divide(portfolioValue, 4, RoundingMode.HALF_UP);
        return percent.compareTo(PORTFOLIO_PERCENT_THRESHOLD) <= 0;
    }

    private boolean hasStrategyTag(TradeJournal trade) {
        return trade.getStrategyTag() != null && !trade.getStrategyTag().isBlank();
    }

    private boolean hasMinimumHoldingPeriod(TradeJournal trade) {
        if (trade.getCloseDate() == null || trade.getTradeDate() == null) {
            return false;
        }
        long days = ChronoUnit.DAYS.between(trade.getTradeDate(), trade.getCloseDate());
        return days >= MIN_HOLDING_DAYS;
    }
    // Return all trades for summary display, ordered by date descending (newest first).
    public List<TradeJournal> getAllTradesForSummary() {
        return repository.findAllByOrderByTradeDateDesc();
    }

    // Return a single trade by ID.
    public TradeJournal getTradeById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Return all trades for a symbol.
    public List<TradeJournal> getTradesBySymbol(String symbol) {
        return repository.findBySymbol(symbol);
    }

    // Update reflection notes for a trade.
    // Returns the updated trade or null if not found.
    // Uses the repository to find the trade by ID and update the notes.
    // Returns the updated trade or null if not found.
    @Transactional
    public TradeJournal updateNotes(Long id, String notes) {
        TradeJournal trade = repository.findById(id).orElse(null);
        if (trade == null) return null;
        trade.setNotes(notes);
        return repository.save(trade);
    }

    // Build behavioral stats summary from all trades.
    public TradeSummary getBehavioralSummary() {
        return buildSummaryFromTrades(repository.findAllByOrderByTradeDateDesc());
    }

    // Build behavioral stats summary from a given list of trades (e.g. for a specific account).
    public TradeSummary buildSummaryFromTrades(List<TradeJournal> trades) {
        int total = trades.size();
        if (total == 0) {
            return new TradeSummary(0, BigDecimal.ZERO, 0, 0, List.of());
        }

        BigDecimal sum = BigDecimal.ZERO;
        int winCount = 0;
        int lossCount = 0;
        Map<String, Integer> violationCounts = new HashMap<>();

        for (TradeJournal t : trades) {
            if (t.getDisciplineScore() != null) {
                sum = sum.add(t.getDisciplineScore());
            }
            if ("WIN".equalsIgnoreCase(t.getResult())) winCount++;
            if ("LOSS".equalsIgnoreCase(t.getResult())) lossCount++;
            if (t.getViolations() != null && !t.getViolations().isBlank()) {
                for (String v : t.getViolations().split(",")) {
                    String trimmed = v.trim();
                    if (!trimmed.isEmpty()) {
                        violationCounts.merge(trimmed, 1, Integer::sum);
                    }
                }
            }
        }

        BigDecimal avgScore = sum.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        List<String> topViolations = violationCounts.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return new TradeSummary(total, avgScore, winCount, lossCount, topViolations);
    }
}
