package com.example.Backend.repository;

import com.example.Backend.entity.TradeJournal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the trade_journal table.
 * Provides methods to query trades by symbol and date ordering.
 */
public interface TradeJournalRepository extends JpaRepository<TradeJournal, Long> {

    /**
     * Find all trades for a given symbol.
     */
    List<TradeJournal> findBySymbol(String symbol);

    /**
     * Find all trades ordered by trade date descending (newest first).
     */
    List<TradeJournal> findAllByOrderByTradeDateDesc();

    /**
     * Find all trades for a given account, ordered by trade date descending.
     */
    List<TradeJournal> findByAccountIdOrderByTradeDateDesc(Long accountId);
}
