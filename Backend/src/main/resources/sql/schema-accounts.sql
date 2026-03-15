-- Accounts for college students to track progress and trades

-- Accounts table: one per student
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    initial_cash NUMERIC(15, 2) NOT NULL DEFAULT 100000.00,
    risk_tolerance VARCHAR(20) CHECK (risk_tolerance IN ('LOW', 'MODERATE', 'HIGH', 'AGGRESSIVE')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Link trades to accounts
ALTER TABLE trade_journal
    ADD COLUMN IF NOT EXISTS account_id BIGINT REFERENCES accounts(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_trade_journal_account_id ON trade_journal (account_id);
