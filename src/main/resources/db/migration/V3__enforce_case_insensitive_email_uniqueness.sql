ALTER TABLE users
    DROP CONSTRAINT IF EXISTS users_email_key;

CREATE UNIQUE INDEX users_email_lower_unique
    ON users (LOWER(email));