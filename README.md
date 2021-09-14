# nazerwow-midterm-gilobank


// SQL DATA // 

INSERT INTO ADDRESS (house_number, street, city, postcode) VALUES
('38', 'Mere Road', 'Stourbridge', 'DY8 2YY'),
('27', 'Ridge Street', 'Sheffield', 'SF8 7HS'),
('99', 'Wind Road', 'Heron', 'HR1 8HB');

INSERT INTO login_details (username, password) VALUES
('hackerman', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e"),
('testLogin2', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e");

INSERT INTO account_holder (id, login_details, first_name, surname, primary_address, date_of_birth, role) VALUES
(1, 1,  'Nathan', 'Giles', 1, '1988-04-05', 'ACCOUNTHOLDER'),
(2, 2, 'Sarah', 'Break', 2, '1990-10-23', 'ACCOUNTHOLDER');

INSERT INTO checking_account (account_number, balance, open_date, penalty_fee, status, primary_holder, secondary_holder, minimum_balance, monthly_maintenance_fee) VALUES
(32165487, 2500.00, '2021-05-05', 40, 'ACTIVE', 1, 2, 0, 12);


