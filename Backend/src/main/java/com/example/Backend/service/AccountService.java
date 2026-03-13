package com.example.Backend.service;

import com.example.Backend.dto.TradeSummary;
import com.example.Backend.entity.Account;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.repository.AccountRepository;
import com.example.Backend.repository.TradeJournalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service for account operations. Handles CRUD and progress summaries for student accounts.
// Use this to get the account ID for a given user.
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TradeJournalRepository tradeJournalRepository;
    private final TradeJournalService tradeJournalService;

    public AccountService(AccountRepository accountRepository,
                          TradeJournalRepository tradeJournalRepository,
                          TradeJournalService tradeJournalService) {
        this.accountRepository = accountRepository;
        this.tradeJournalRepository = tradeJournalRepository;
        this.tradeJournalService = tradeJournalService;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public List<TradeJournal> getTradesForAccount(Long accountId) {
        return tradeJournalRepository.findByAccountIdOrderByTradeDateDesc(accountId);
    }

    public TradeSummary getProgressForAccount(Long accountId) {
        List<TradeJournal> trades = tradeJournalRepository.findByAccountIdOrderByTradeDateDesc(accountId);
        return tradeJournalService.buildSummaryFromTrades(trades);
    }
}
