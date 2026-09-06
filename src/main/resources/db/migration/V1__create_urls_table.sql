CREATE TABLE urls (
                      id BIGSERIAL PRIMARY KEY,
                      short_code VARCHAR(10) NOT NULL UNIQUE,
                      original_url TEXT NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      expires_at TIMESTAMP NULL
);

CREATE INDEX idx_urls_short_code ON urls(short_code);