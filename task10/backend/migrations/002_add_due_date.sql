-- 002_add_due_date.sql
-- Add nullable due_date column for phased rollout
ALTER TABLE todos ADD COLUMN due_date TEXT NULL;