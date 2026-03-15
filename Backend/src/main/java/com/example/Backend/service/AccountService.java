package com.example.Backend.service;

import com.example.Backend.dto.TradeSummary;
import com.example.Backend.entity.Account;
import com.example.Backend.entity.RiskTolerance;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.repository.AccountRepository;
import com.example.Backend.repository.TradeJournalRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          TradeJournalRepository tradeJournalRepository,
                          TradeJournalService tradeJournalService,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.tradeJournalRepository = tradeJournalRepository;
        this.tradeJournalService = tradeJournalService;
        this.passwordEncoder = passwordEncoder;
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

    // Find an account by username.
    // Used for authentication.
    // Returns Optional<Account> because the account may not exist.
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account registerAccount(String username, String password, String displayName, RiskTolerance riskTolerance) {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
        Account account = new Account();
        account.setUsername(username);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setDisplayName(displayName);
        account.setRiskTolerance(riskTolerance);
        return accountRepository.save(account);
    }

    public Optional<Account> authenticateUser(String username, String rawPassword) {
        return accountRepository.findByUsername(username)
                .filter(account -> passwordEncoder.matches(rawPassword, account.getPasswordHash()));
    }

    public List<TradeJournal> getTradesForAccount(Long accountId) {
        return tradeJournalRepository.findByAccountIdOrderByTradeDateDesc(accountId);
    }

    public TradeSummary getProgressForAccount(Long accountId) {
        List<TradeJournal> trades = tradeJournalRepository.findByAccountIdOrderByTradeDateDesc(accountId);
        return tradeJournalService.buildSummaryFromTrades(trades);
    }
}
