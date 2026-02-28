package com.example.Backend.repository;

import com.example.Backend.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

// Repository for the price_history table. Spring Data JPA provides save(), findAll(), findById(), etc. automatically. Custom query methods are derived from method names - no implementation needed.

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    // Find all price records for a given symbol within a date range (inclusive). Used for charting historical price data.
    List<PriceHistory> findBySymbolAndTradeDateBetween(String symbol, LocalDate start, LocalDate end);

    // Get all distinct stock symbols in the database. Used to populate symbol dropdown/autocomplete in the UI.
    // Uses a query to select distinct symbols from the price_history table.
    // Returns a list of strings, each representing a symbol.
    // Uses the repository to find all distinct symbols and return them.
    // Returns a list of strings, each representing a symbol.
    @Query("SELECT DISTINCT p.symbol FROM PriceHistory p ORDER BY p.symbol")
    List<String> findDistinctSymbols();
}
