CREATE TABLE url_analytics (
                               id BIGINT PRIMARY KEY,
                               short_code VARCHAR(20) NOT NULL,
                               event_type VARCHAR(50) NOT NULL,
                               original_url TEXT NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                               processed_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_url_analytics_short_code
    ON url_analytics(short_code);

CREATE INDEX idx_url_analytics_created_at
    ON url_analytics(created_at);