package com.example.Backend;

import com.example.Backend.entity.Account;
import com.example.Backend.entity.TradeJournal;
import com.example.Backend.service.AccountService;
import com.example.Backend.service.PriceHistoryService;
import com.example.Backend.service.TradeJournalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private PriceHistoryService priceHistoryService;

	@Autowired
	private TradeJournalService tradeJournalService;

	@Autowired
	private AccountService accountService;

	@Test
	void contextLoads() {
	}

	@Test
	void symbolsEndpointWorks() {
		List<String> symbols = priceHistoryService.getAllSymbols();
		// Call succeeds; list may be empty if CSV not loaded
		assert symbols != null;
	}

	@Test
	void pricesQueryWorks() {
		List<?> prices = priceHistoryService.getPriceHistoryBySymbolAndDateRange("AAPL",
				LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1));
		assert prices != null;
	}

	@Test
	void tradeJournalWorks() {
		var summary = tradeJournalService.getBehavioralSummary();
		assert summary != null;
		assert summary.getTotalTrades() >= 0;

		TradeJournal trade = new TradeJournal();
		trade.setSymbol("TEST");
		trade.setTradeType(TradeJournal.TradeType.BUY);
		trade.setShares(new BigDecimal("10"));
		trade.setEntryPrice(new BigDecimal("100"));
		trade.setTradeDate(LocalDate.now());
		trade.setThesis("Test thesis");
		trade.setStopLoss(new BigDecimal("95"));
		trade.setTargetPrice(new BigDecimal("110"));
		trade.setStrategyTag("test");
		trade.setCloseDate(LocalDate.now().plusDays(1));

		TradeJournal saved = tradeJournalService.logTrade(trade, new BigDecimal("100000"));
		assert saved != null;
		assert saved.getId() != null;
		assert saved.getDisciplineScore() != null;
	}

	@Test
	void accountCrudWorks() {
		Account account = new Account();
		account.setDisplayName("Test Student");
		account.setEmail("test@example.edu");

		Account saved = accountService.create(account);
		assert saved != null;
		assert saved.getId() != null;
		assert "Test Student".equals(saved.getDisplayName());

		var found = accountService.findById(saved.getId());
		assert found.isPresent();
		assert "Test Student".equals(found.get().getDisplayName());

		var trades = accountService.getTradesForAccount(saved.getId());
		assert trades != null;

		var progress = accountService.getProgressForAccount(saved.getId());
		assert progress != null;
		assert progress.getTotalTrades() >= 0;
	}
}
