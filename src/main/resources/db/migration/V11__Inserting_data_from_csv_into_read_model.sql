-- Seeds cafe_read_model with rating statistics (mean/std) derived from CoffeeData_-_Rates.csv
-- Price columns intentionally left NULL; populated later via POST /admin/backfill
-- This allows DataSeeder to read avg/std values immediately on a fresh database

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.8, 0.7611, 4.9667, 0.1826, 4.9333, 0.2537 FROM cafes WHERE name = 'Aftertaste Coffee & Wine';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.8, 0.4842, 4.6, 1.1326, 4.8667, 0.4342 FROM cafes WHERE name = 'Ambalaż - kawiarnia z widokiem';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.6923, 0.8376, 4.7308, 0.8274, 4.7692, 0.8152 FROM cafes WHERE name = 'Blumen Café';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.381, 1.1609, 4.2857, 1.347, 4.0952, 1.5461 FROM cafes WHERE name = 'B.O.H.O Coffee&Bar';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.5333, 1.1366, 4.4667, 1.1666, 4.5667, 1.0063 FROM cafes WHERE name = 'Bonbon Boutique';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.7619, 0.7003, 4.4762, 1.0779, 4.2381, 1.0443 FROM cafes WHERE name = 'Butter Café';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.92, 0.2769, 4.92, 0.2769, 4.96, 0.2 FROM cafes WHERE name = 'Cafe Kładka';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.619, 0.74, 4.4762, 1.0305, 4.1905, 1.1233 FROM cafes WHERE name = 'Cakester Cafe';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.72, 0.8426, 4.84, 0.4726, 4.32, 1.1804 FROM cafes WHERE name = 'Carmel Atelier';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.619, 0.669, 4.7143, 0.5606, 4.8095, 0.4024 FROM cafes WHERE name = 'Cawa';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.0952, 1.2611, 3.9048, 1.7001, 4.0952, 1.4108 FROM cafes WHERE name = 'Charlotte';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.7, 0.9154, 4.7667, 0.8172, 4.7333, 0.8277 FROM cafes WHERE name = 'Coffeece';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.64, 0.9074, 4.6923, 0.884, 4.6923, 0.7359 FROM cafes WHERE name = 'COFFEE GARDEN';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.5238, 1.0305, 4.2174, 1.2044, 4.6957, 0.7648 FROM cafes WHERE name = 'CYTAT Café';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.4286, 1.2479, 4.5238, 1.0779, 4.0952, 1.3002 FROM cafes WHERE name = 'De revolutionibus ideæ';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.3333, 1.0165, 4.4762, 1.0305, 4.5714, 0.9258 FROM cafes WHERE name = 'Dziórawy Kocioł';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.4545, 0.6876, 4.4545, 0.8202, 4.7273, 0.4671 FROM cafes WHERE name = 'Efemeria. Kraina Czarów';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.4643, 1.1701, 4.4286, 1.1996, 4.5714, 0.9595 FROM cafes WHERE name = 'EMIGRANT Specialty Coffee – Kalwaryjska';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.0952, 1.5461, 4.1905, 1.2891, 4.381, 1.0235 FROM cafes WHERE name = 'Fable Cafe';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.9583, 0.2041, 4.8, 0.5, 4.72, 0.6782 FROM cafes WHERE name = 'FINCA COFFEE';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.6, 0.9322, 4.4667, 1.306, 4.3333, 1.5162 FROM cafes WHERE name = 'iOVE Cocktail I Coffee I Music I Fashion I Breakfast';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.3478, 1.2288, 4.7826, 0.5184, 4.7391, 0.5408 FROM cafes WHERE name = 'KAFFE BAGERI Stockholm';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 3.9524, 1.5322, 4.0, 1.5492, 4.0476, 1.3956 FROM cafes WHERE name = 'Katane';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.5385, 1.1038, 4.7407, 0.813, 4.6538, 0.7452 FROM cafes WHERE name = 'KawaLerka';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 3.9677, 1.0796, 4.5161, 0.9616, 4.5484, 1.0276 FROM cafes WHERE name = 'La Baguette';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.6333, 0.8087, 4.6333, 0.8899, 4.7, 0.7944 FROM cafes WHERE name = 'Mech Café';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.4583, 1.0624, 4.4615, 0.9892, 4.8077, 0.801 FROM cafes WHERE name = 'Mleczarnia';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.7619, 0.539, 4.2381, 1.0443, 4.3333, 0.9129 FROM cafes WHERE name = 'Nowa Prowincja';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.4, 1.3797, 4.8667, 0.4342, 4.9, 0.3051 FROM cafes WHERE name = 'Rozkwit cafe';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.5333, 1.0417, 4.6, 0.9685, 4.7333, 0.6915 FROM cafes WHERE name = 'Søtt Cafe & Bakery • Specialty Coffee • Breakfasts';

INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.5238, 0.8729, 4.5714, 0.8701, 4.8571, 0.4781 FROM cafes WHERE name = 'Żarówka Cafe';

-- Urban Coffee has two locations in the DB; both get the same chain-level stats
INSERT INTO cafe_read_model (cafe_id, avg_beverage_score, std_dev_beverage, avg_service_score, std_dev_service, avg_atmosphere_score, std_dev_atmosphere)
SELECT id, 4.6786, 0.8551, 4.7321, 0.8419, 4.7143, 0.8467 FROM cafes WHERE name = 'Urban Coffee';