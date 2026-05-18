ALTER TABLE bookings
    ADD COLUMN notification_dismissed BOOLEAN NOT NULL DEFAULT FALSE;
