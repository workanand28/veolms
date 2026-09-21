ALTER TABLE users
    ALTER COLUMN password_hash DROP NOT NULL,
    ALTER COLUMN password_hash DROP DEFAULT;

UPDATE users
SET password_hash = NULL
WHERE password_hash = 'TEMPORARY_HASH';