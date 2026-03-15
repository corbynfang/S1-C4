package com.example.Backend.controller;

import com.example.Backend.dto.TradeSummary;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.service.TradeJournalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// REST controller for trade journal endpoints. Handles logging trades, updating notes, and retrieving behavioral stats.

@RestController
@RequestMapping("/api/trades")
public class TradeJournalController {

    private final TradeJournalService tradeJournalService;

    public TradeJournalController(TradeJournalService tradeJournalService) {
        this.tradeJournalService = tradeJournalService;
    }

    // Log a new trade.
    // Uses the service to log the trade.
    // Returns the saved trade.
    // Uses the service to log the trade and return the saved trade.
    // Returns the saved trade.
    @PostMapping
    public ResponseEntity<TradeJournal> logTrade(
            @RequestBody TradeJournal trade,
            @RequestParam(required = false, defaultValue = "0") BigDecimal portfolioValue) {
        TradeJournal saved = tradeJournalService.logTrade(trade, portfolioValue);
        return ResponseEntity.ok(saved);
    }

    // Return all trades, ordered by date descending.
    // Uses the service to find all trades.
    // Returns a list of TradeJournal entities, ordered by trade date.
    // Uses the service to find all trades and return them.
    // Returns a list of TradeJournal entities, ordered by trade date.
    @GetMapping
    public ResponseEntity<List<TradeJournal>> getAllTrades() {
        List<TradeJournal> trades = tradeJournalService.getAllTradesForSummary();
        return ResponseEntity.ok(trades);
    }

    // Return behavioral stats: total trades, avg discipline score, win/loss counts, top violations.
    // Uses the service to find the behavioral summary.
    // Returns a TradeSummary object with the total trades, average discipline score, win/loss counts, and top violations.
    // Uses the service to find the behavioral summary and return it.
    // Returns a TradeSummary object with the total trades, average discipline score, win/loss counts, and top violations.
    @GetMapping("/summary")
    public ResponseEntity<TradeSummary> getSummary() {
        TradeSummary summary = tradeJournalService.getBehavioralSummary();
        return ResponseEntity.ok(summary);
    }

    // Return a single trade by ID.
    // Uses the service to find the trade by ID.
    // Returns the TradeJournal entity or null if not found.
    // Uses the service to find the trade by ID and return it.
    // Returns the TradeJournal entity or null if not found.
    @GetMapping("/{id}")
    public ResponseEntity<TradeJournal> getTrade(@PathVariable Long id) {
        TradeJournal trade = tradeJournalService.getTradeById(id);
        if (trade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trade);
    }

    // Update reflection notes for a trade.
    // Uses the service to update the notes.
    // Returns the updated trade or null if not found.
    // Uses the service to update the notes and return the updated trade.
    // Returns the updated trade or null if not found.
    @PutMapping("/{id}/notes")
    public ResponseEntity<TradeJournal> updateNotes(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String notes = body != null ? body.get("notes") : null;
        if (notes == null) notes = "";
        TradeJournal updated = tradeJournalService.updateNotes(id, notes);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}
