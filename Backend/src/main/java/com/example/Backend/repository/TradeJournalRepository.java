package com.example.Backend.repository;

import com.example.Backend.entity.TradeJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository for the trade_journal table. Provides CRUD operations for trade journal entries.
// Use this to get the trade journal for a given symbol, account, or all accounts.
public interface TradeJournalRepository extends JpaRepository<TradeJournal, Long> {

    // Find all trades for a given symbol.
    // Used to get the trade journal for a given symbol.
    List<TradeJournal> findBySymbol(String symbol);

    // Find all trades ordered by trade date descending (newest first).
    // Used to get the trade journal for all accounts.
    List<TradeJournal> findAllByOrderByTradeDateDesc();

    // Find all trades for a given account, ordered by trade date descending.
    // Used to get the trade journal for a given account.
    List<TradeJournal> findByAccountIdOrderByTradeDateDesc(Long accountId);
}
