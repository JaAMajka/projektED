CREATE TABLE IF NOT EXISTS cafes (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  name VARCHAR NOT NULL,
                                  address VARCHAR NOT NULL,
                                  has_wifi BOOLEAN NOT NULL DEFAULT FALSE,
                                  allows_pets BOOLEAN NOT NULL DEFAULT FALSE,
                                  sells_food BOOLEAN NOT NULL DEFAULT FALSE,
                                  allows_students_discounts BOOLEAN NOT NULL DEFAULT FALSE,
                                  is_lgbtq_friendly BOOLEAN, has_toilet BOOLEAN NOT NULL DEFAULT FALSE,
                                  has_terrace BOOLEAN NOT NULL DEFAULT FALSE,
                                  allows_intake BOOLEAN NOT NULL DEFAULT FALSE,
                                  allows_takeaway BOOLEAN NOT NULL DEFAULT FALSE,
                                  supports_card_payments BOOLEAN NOT NULL DEFAULT FALSE,
                                  created_at TIMESTAMP NOT NULL DEFAULT NOW());


CREATE TABLE IF NOT EXISTS beverage_types (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                           name VARCHAR NOT NULL,
                                           TYPE VARCHAR NOT NULL,
                                           created_at TIMESTAMP NOT NULL DEFAULT NOW());


CREATE TABLE IF NOT EXISTS menu_items
(id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 name VARCHAR NOT NULL,
 cafe_id INTEGER NOT NULL,
 beverage_id INTEGER NOT NULL,
 price FLOAT NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_menu_items_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_items_beverage
    FOREIGN KEY (beverage_id) REFERENCES beverage_types(id) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS users (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  name VARCHAR NOT NULL,
                                  email VARCHAR NOT NULL UNIQUE,
                                  phone_number VARCHAR NOT NULL UNIQUE,
                                  password_hash VARCHAR NOT NULL,
                                  is_student BOOLEAN NOT NULL DEFAULT FALSE,
                                  prefers_card_payment BOOLEAN NOT NULL DEFAULT FALSE,
                                  created_at TIMESTAMP NOT NULL DEFAULT NOW());


CREATE TABLE IF NOT EXISTS rates
(cafe_id INTEGER NOT NULL,
 user_id INTEGER NOT NULL,
 beverage_score INTEGER, service_score INTEGER, atmosphere_score INTEGER, created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id,
                 cafe_id), CONSTRAINT fk_rates_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_rates_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE,
    CONSTRAINT at_least_one_score CHECK (beverage_score IS NOT NULL
                                         OR service_score IS NOT NULL
                                         OR atmosphere_score IS NOT NULL));


CREATE TABLE IF NOT EXISTS recommendations
(id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 user_id INTEGER NOT NULL,
 cafe_id INTEGER NOT NULL,
 score INTEGER NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_recommendations_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_recommendations_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS schedules
(id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 cafe_id INTEGER NOT NULL,
 week_day VARCHAR NOT NULL,
 opening_hour TIME NOT NULL,
 closing_hour TIME NOT NULL,
 CONSTRAINT fk_schedules_cafe
 FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE);