INSERT INTO users (name, email, phone_number, password_hash, is_student, prefers_card_payment, role, created_at)
VALUES (
           'Admin',
           'admin@test.com',
           '000000000',
           '$2b$10$rjUZtTP6wMW9AcNNN2oJ.OqW34D9sbu.IyjraC3H/fLhdmPSW79wi',
           false,
           false,
           'ADMIN',
           NOW()
       );