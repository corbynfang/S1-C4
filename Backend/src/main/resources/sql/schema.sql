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