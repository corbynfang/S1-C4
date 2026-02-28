-- Simple SQL script for creating the tables for the datasets we have installed in the database

CREATE TABLE IF NOT EXISTS securities (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(255),
    type VARCHAR(20) CHECK (type IN ('STOCK', 'ETF', 'BOND_ETF')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS price_history (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    trade_date DATE NOT NULL,
    open NUMERIC(15, 6) NOT NULL,
    high NUMERIC(15, 6) NOT NULL,
    low NUMERIC(15, 6) NOT NULL,
    close NUMERIC(15, 6) NOT NULL,
    volume NUMERIC(20, 6) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (symbol, trade_date)
);

CREATE INDEX IF NOT EXISTS idx_price_history_symbol ON price_history (symbol);
CREATE INDEX IF NOT EXISTS idx_price_history_date ON price_history (trade_date);
CREATE INDEX IF NOT EXISTS idx_price_history_symbol_date ON price_history (symbol, trade_date);

-- Trade journal for logging trades with discipline scoring
CREATE TABLE IF NOT EXISTS trade_journal (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    trade_type VARCHAR(4) NOT NULL CHECK (trade_type IN ('BUY', 'SELL')),
    shares NUMERIC(15, 4) NOT NULL,
    entry_price NUMERIC(15, 6) NOT NULL,
    exit_price NUMERIC(15, 6),
    stop_loss NUMERIC(15, 6),
    target_price NUMERIC(15, 6),
    thesis VARCHAR(2000),
    notes VARCHAR(2000),
    strategy_tag VARCHAR(50),
    trade_date DATE NOT NULL,
    close_date DATE,
    result VARCHAR(20),
    discipline_score NUMERIC(5, 2),
    violations VARCHAR(500)
);

CREATE INDEX IF NOT EXISTS idx_trade_journal_symbol ON trade_journal (symbol);
CREATE INDEX IF NOT EXISTS idx_trade_journal_trade_date ON trade_journal (trade_date);