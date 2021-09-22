# nazerwow-midterm-gilobank


// SQL DATA // 

INSERT INTO ADDRESS (house_number, street, city, postcode) VALUES
('38', 'Mere Road', 'Stourbridge', 'DY8 2YY'),
('27', 'Ridge Street', 'Sheffield', 'SF8 7HS'),
('99', 'Wind Road', 'Heron', 'HR1 8HB');

INSERT INTO account_holder (id, first_name, surname, primary_address, date_of_birth, role) VALUES
(1, 'Nathan', 'Giles', 1, '1988-04-05', "ACCOUNTHOLDER"),
(2, 'Sarah', 'Break', 2, '1990-10-23', "ACCOUNTHOLDER");

INSERT INTO admin (id, name, role) VALUES
(3, 'admin', 'ADMIN');

INSERT INTO third_party (id, name, hashed_key, role) VALUES
(4, 'third_party', 'hashedkey', 'THIRDPARTY');

INSERT INTO login_details (id, username, password, user_id) VALUES
(1, "hackerman", "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 1),
(2, 'testLogin2', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 2),
(3, 'admin', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 3);
(4, 'thirdparty', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 4);

INSERT INTO checking_account (account_number, balance, open_date, penalty_fee, status, primary_holder, secondary_holder, minimum_balance, monthly_maintenance_fee) VALUES
(32165487, 2500.00, '2021-05-05', 40, 'ACTIVE', 1, 2, 0, 12),
(32165488, 2500.00, '2021-05-05', 40, 'ACTIVE', 2, 1, 0, 12),
(32165489, 2500.00, '2021-05-05', 40, 'ACTIVE', 1, null, 0, 12),
(32165481, 2500.00, '2021-05-05', 40, 'ACTIVE', 2, null, 0, 12);

INSERT INTO transaction (id, amount, balance_after_transaction, name, time_of_trns, type, account_id) VALUES
-- Day 1 - Total debit = 300.00
(1, 100.00, 900.00, '250 Debit', '2021-09-17 08:00:00', 'DEBIT' ,32165487),
(2, 200.00, 700.00, '200 Debit', '2021-09-17 09:00:00', 'DEBIT' ,32165487),
-- Day 2 - Total Debit = 1200
(3, 300.00, 900.00, '300 Debit', '2021-09-15 08:00:00', 'DEBIT' ,32165487),
(4, 400.00, 500.00, '400 Debit', '2021-09-15 10:00:00', 'DEBIT' ,32165487),
(5, 500.00, 0.00, '500 Debit', '2021-09-15 11:00:00', 'DEBIT' ,32165487),
-- Day 3 - Total Debit = 2000
(6, 500.00, 2500.00, '500 Debit', '2021-09-13 06:00:00', 'DEBIT' ,32165487),
(7, 500.00, 2000.00, '500 Debit', '2021-09-13 08:35:20', 'DEBIT' ,32165487),
(8, 500.00, 1500.00, '500 Debit', '2021-09-13 08:00:21', 'DEBIT' ,32165487),
(9, 500.00, 1000.00, '500 Debit', '2021-09-13 12:00:59', 'DEBIT' ,32165487),

-- Credits
(10, 250.00, 750.00, '250 Debit', '2021-09-14 08:00:00', 'CREDIT' ,32165487),
(11, 250.00, 750.00, '250 Debit', '2021-09-17 08:11:00', 'CREDIT' ,32165487),
(12, 250.00, 750.00, '250 Debit', '2021-09-11 08:50:00', 'CREDIT' ,32165487);

-- 3 Transactions within the same second
INSERT INTO transaction (id, amount, balance_after_transaction, name, time_of_trns, type, account_id) VALUES
(13, 100.00, 900.00, '250 Debit', '2021-09-17 11:00:00', 'DEBIT' ,32165487),
(14, 100.00, 900.00, '250 Debit', '2021-09-17 11:00:00', 'DEBIT' ,32165487),
(15, 100.00, 900.00, '250 Debit', '2021-09-17 11:00:00', 'DEBIT' ,32165487);

-- 3 other
INSERT INTO transaction (id, amount, balance_after_transaction, name, time_of_trns, type, account_id) VALUES
(16, 100.00, 900.00, '250 Debit', '2021-09-17 11:00:05', 'DEBIT' ,32165487),
(17, 100.00, 900.00, '250 Debit', '2021-09-17 11:00:05', 'DEBIT' ,32165487),
(18, 100.00, 900.00, '250 Debit', '2021-09-17 11:00:05', 'DEBIT' ,32165487);

-- 3 other
INSERT INTO transaction (id, amount, balance_after_transaction, name, time_of_trns, type, account_id) VALUES
(19, 100.00, 900.00, '250 Debit', '2021-09-16 10:00:05', 'INTEREST_CREDIT' ,32165487),
(20, 100.00, 900.00, '250 Debit', '2021-08- 10:00:05', 'INTEREST_DEBIT' ,32165487),
(21, 100.00, 900.00, '250 Debit', '2021-07-17 10:00:05', 'DEBIT' ,32165487);

INSERT INTO transaction (id, amount, balance_after_transaction, name, time_of_trns, type, account_id) VALUES
(22, 100.00, 900.00, '250 Debit', '2021-08-19 10:00:05', 'INTEREST_CREDIT' ,32165487),
(32, 100.00, 900.00, '250 Debit', '2021-08-20 08:25:05', 'INTEREST_DEBIT' ,32165487),
(42, 100.00, 900.00, '250 Debit', '2021-08-20 08:23:05', 'INTEREST_DEBIT' ,32165487);

