ALTER TABLE bookings
    ADD COLUMN user_id BIGINT NULL,
    ADD CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users (id);
