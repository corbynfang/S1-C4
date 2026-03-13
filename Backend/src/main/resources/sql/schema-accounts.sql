-- Accounts for college students to track progress and trades
-- Run this on an existing database: psql -U your_user -d s1c4database -f src/main/resources/sql/schema-accounts.sql

-- Accounts table: one per student
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    display_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    initial_cash NUMERIC(15, 2) NOT NULL DEFAULT 100000.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Link trades to accounts (nullable for backward compatibility with existing trades)
ALTER TABLE trade_journal
    ADD COLUMN IF NOT EXISTS account_id BIGINT REFERENCES accounts(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_trade_journal_account_id ON trade_journal (account_id);
