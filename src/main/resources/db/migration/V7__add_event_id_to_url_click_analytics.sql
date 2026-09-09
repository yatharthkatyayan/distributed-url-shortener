ALTER TABLE url_click_analytics
    ADD COLUMN event_id UUID;

UPDATE url_click_analytics
SET event_id = gen_random_uuid()
WHERE event_id IS NULL;

ALTER TABLE url_click_analytics
    ALTER COLUMN event_id SET NOT NULL;

ALTER TABLE url_click_analytics
    ADD CONSTRAINT uk_url_click_analytics_event_id
        UNIQUE (event_id);