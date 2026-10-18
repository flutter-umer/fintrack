-- FinTrack Backend — database schema
--
-- ddl-auto is set to 'none'. Run this script manually against your PostgreSQL
-- database before starting the application for the first time (Milestone 3+).
--
-- Usage:
--   psql -U fintrack_user -d fintrack -f src/main/resources/db/schema.sql
--
-- Re-running this script is safe: all statements use IF NOT EXISTS.

CREATE TABLE IF NOT EXISTS transactions (
    id               BIGSERIAL        PRIMARY KEY,
    owner_uid        VARCHAR(128)     NOT NULL,
    type             VARCHAR(10)      NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    amount           NUMERIC(19, 4)   NOT NULL CHECK (amount > 0),
    description      VARCHAR(255)     NOT NULL,
    category         VARCHAR(100)     NOT NULL,
    transaction_date DATE             NOT NULL,
    created_at       TIMESTAMPTZ      NOT NULL DEFAULT NOW()
);

-- Index for the most common query: list transactions for one user, ordered by date.
CREATE INDEX IF NOT EXISTS idx_transactions_owner_date
    ON transactions (owner_uid, transaction_date DESC);
