CREATE TABLE global_stats (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    avg_price_pure_coffee NUMERIC(10, 2),
    avg_price_coffee_drinks NUMERIC(10, 2),
    avg_price_matcha NUMERIC(10, 2),
    avg_price_bagged_tea NUMERIC(10, 2),
    avg_price_fruity_drink NUMERIC(10, 2),
    avg_price_leaf_tea NUMERIC(10, 2),
    avg_price_other NUMERIC(10, 2),
    std_dev_atmosphere NUMERIC(10, 2),
    std_dev_beverage NUMERIC(10, 2),
    std_dev_service NUMERIC(10, 2),
    updated_at TIMESTAMP
);