ALTER TABLE bookings
    ADD COLUMN destination_name VARCHAR(255) NULL,
    ADD COLUMN trip_title VARCHAR(255) NULL;

UPDATE bookings b
SET trip_title = 'Tour cùng ' || g.name
FROM guides g
WHERE g.id = b.guide_id
  AND b.trip_title IS NULL
  AND b.guide_id IS NOT NULL;
