-- Chạy khi Flyway báo "failed migration to version 6 (users)"
-- Cách 1 (khuyên dùng): restart backend — app.flyway.repair-on-start=true sẽ tự repair.
-- Cách 2: chạy SQL này trong psql / pgAdmin rồi restart.

\c vietlocal

DROP TABLE IF EXISTS users CASCADE;

DELETE FROM flyway_schema_history WHERE version = '6';
