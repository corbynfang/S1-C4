package com.example.Backend.controller;

import com.example.Backend.entity.PriceHistory;
import com.example.Backend.service.PriceHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// REST controller for price history and symbol endpoints. Serves data for the frontend chart and symbol selector.

@RestController
@RequestMapping("/api")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    // Return price history for a symbol within the given date range.
    // Uses the service to find the price history records by symbol and date range.
    // Returns a list of PriceHistory entities, ordered by trade date.
    // Uses the service to find the price history records by symbol and date range and return them.
    // Returns a list of PriceHistory entities, ordered by trade date.
    @GetMapping("/prices")
    public ResponseEntity<List<PriceHistory>> getPrices(
            @RequestParam String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<PriceHistory> prices = priceHistoryService.getPriceHistoryBySymbolAndDateRange(symbol, start, end);
        return ResponseEntity.ok(prices);
    }

    // Return all distinct stock symbols available in the database.
    // Uses the service to find all distinct symbols.
    // Returns a list of strings, each representing a symbol.
    // Uses the service to find all distinct symbols and return them.
    // Returns a list of strings, each representing a symbol.
    @GetMapping("/symbols")
    public ResponseEntity<List<String>> getSymbols() {
        List<String> symbols = priceHistoryService.getAllSymbols();
        return ResponseEntity.ok(symbols);
    }
}
