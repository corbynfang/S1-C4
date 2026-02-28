package com.example.Backend.service;

import com.example.Backend.entity.PriceHistory;
import com.example.Backend.repository.PriceHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Service layer for price history data. Provides business logic for fetching price data by symbol and date range, and for retrieving the list of available symbols.

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository repository;

    public PriceHistoryService(PriceHistoryRepository repository) {
        this.repository = repository;
    }

    // Get all price history records for a symbol within the given date range.
    // Uses the repository to find the price history records by symbol and date range.
    // Returns a list of PriceHistory entities, ordered by trade date.
    public List<PriceHistory> getPriceHistoryBySymbolAndDateRange(String symbol, LocalDate start, LocalDate end) {
        return repository.findBySymbolAndTradeDateBetween(symbol, start, end);
    }

    // Get all distinct stock symbols available in the database.
    // Uses the repository to find all distinct symbols.
    // Returns a list of strings, each representing a symbol.
    // Uses the repository to find all distinct symbols and return them.
    // Returns a list of strings, each representing a symbol.
    public List<String> getAllSymbols() {
        return repository.findDistinctSymbols();
    }
}
