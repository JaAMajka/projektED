CREATE TABLE cafe_read_model (
                                 id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 avg_coffee_price NUMERIC(10, 2),
                                 avg_coffee_drinks NUMERIC(10, 2),
                                 avg_matcha_price NUMERIC(10, 2),
                                 avg_bagged_tea_price NUMERIC(10, 2),
                                 avg_fruity_drink_price NUMERIC(10, 2),
                                 avg_leaf_tea_price NUMERIC(10, 2),
                                 avg_other_price NUMERIC(10, 2),
                                 has_iced_items BOOLEAN,
                                 avg_beverage_score NUMERIC(10, 2),
                                 avg_service_score NUMERIC(10, 2),
                                 avg_atmosphere_score NUMERIC(10, 2),
                                 cafe_id INTEGER NOT NULL UNIQUE,
                                 CONSTRAINT fk_read_model_cafe
                                     FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE,
                                 is_open_on_weekdays BOOLEAN,
                                 is_open_on_weekends BOOLEAN
);