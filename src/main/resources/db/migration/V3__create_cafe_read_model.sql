CREATE TABLE cafe_read_model (
                                 id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 cafe_id INTEGER NOT NULL UNIQUE,
                                 avg_coffee_price NUMERIC(10, 2),
                                 avg_white_coffee_price NUMERIC(10, 2),
                                 avg_matcha_price NUMERIC(10, 2),
                                 avg_bagged_tea_price NUMERIC(10, 2),
                                 avg_fruity_drink_price NUMERIC(10, 2),
                                 avg_leaf_tea_price NUMERIC(10, 2),
                                 avg_other_price NUMERIC(10, 2),
                                 has_iced_items BOOLEAN,
                                 avg_beverage_score NUMERIC(10, 2),
                                 avg_service_score NUMERIC(10, 2),
                                 avg_atmosphere_score NUMERIC(10, 2),
                                 cluster_id INTEGER,
                                 created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                 updated_at TIMESTAMP,
                                 CONSTRAINT fk_read_model_cafe
                                     FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE
);