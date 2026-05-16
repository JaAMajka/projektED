CREATE TABLE IF NOT EXISTS cafes (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    has_wifi BOOLEAN,
    allows_pets BOOLEAN,
    sells_food BOOLEAN,
    allows_students_discounts BOOLEAN,
    is_lgbtq_friendly BOOLEAN,
    has_toilet BOOLEAN,
    has_terrace BOOLEAN,
    allows_intake BOOLEAN,
    allows_takeaway BOOLEAN,
    supports_card_payments BOOLEAN,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TYPE BEVERAGE_TYPE as ENUM (
    'COFFEE',
    'COFFEE_DRINKS',
    'MATCHA',
    'BAGGED_TEA',
    'FRUITY_DRINK',
    'LEAF_TEA',
    'OTHER'
);

CREATE TYPE AVAILABLE_SIZE as ENUM (
    'S',
    'L'
);

CREATE TABLE IF NOT EXISTS menu_items (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    cafe_id INTEGER NOT NULL,
    type BEVERAGE_TYPE NOT NULL,
    size AVAILABLE_SIZE,
    is_appendage BOOLEAN NOT NULL,
    is_iced BOOLEAN,
    capacity INTEGER,
    price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    CONSTRAINT fk_menu_items_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    phone_number VARCHAR NOT NULL UNIQUE,
    password_hash VARCHAR NOT NULL,
    is_student BOOLEAN NOT NULL DEFAULT FALSE,
    prefers_card_payment BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP
);


CREATE TABLE IF NOT EXISTS rates (
    cafe_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    beverage_score INTEGER,
    service_score INTEGER,
    atmosphere_score INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    PRIMARY KEY (
        user_id,
        cafe_id
    ),
    CONSTRAINT fk_rates_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_rates_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE,

    CONSTRAINT at_least_one_score CHECK (
        beverage_score IS NOT NULL
        OR service_score IS NOT NULL
        OR atmosphere_score IS NOT NULL
    )
);


CREATE TABLE IF NOT EXISTS recommendations (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    cafe_id INTEGER NOT NULL,
    score INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,

    CONSTRAINT fk_recommendations_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_recommendations_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE
);

CREATE TYPE WEEKDAY as ENUM (
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY'
);
CREATE TABLE IF NOT EXISTS schedules (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cafe_id INTEGER NOT NULL,
    week_day WEEKDAY NOT NULL,
    opening_hour TIME NOT NULL,
    closing_hour TIME NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_schedules_cafe
    FOREIGN KEY (cafe_id) REFERENCES cafes(id) ON DELETE CASCADE
);

