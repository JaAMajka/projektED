CREATE TABLE global_stats (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    beverage_type VARCHAR NOT NULL UNIQUE,
    avg_price NUMERIC(10, 2),
    avg_beverage_score NUMERIC(10, 2),
    avg_service_score NUMERIC(10, 2),
    avg_atmosphere_score NUMERIC(10, 2),
    updated_at TIMESTAMP
);