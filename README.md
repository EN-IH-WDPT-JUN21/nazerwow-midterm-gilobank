# nazerwow-midterm-gilobank

<pre>
   █████████   ███  █████          ███████       ███████████    █████████   ██████   █████ █████   ████
  ███░░░░░███ ░░░  ░░███         ███░░░░░███    ░░███░░░░░███  ███░░░░░███ ░░██████ ░░███ ░░███   ███░ 
 ███     ░░░  ████  ░███        ███     ░░███    ░███    ░███ ░███    ░███  ░███░███ ░███  ░███  ███   
░███         ░░███  ░███       ░███      ░███    ░██████████  ░███████████  ░███░░███░███  ░███████    
░███    █████ ░███  ░███       ░███      ░███    ░███░░░░░███ ░███░░░░░███  ░███ ░░██████  ░███░░███   
░░███  ░░███  ░███  ░███      █░░███     ███     ░███    ░███ ░███    ░███  ░███  ░░█████  ░███ ░░███  
 ░░█████████  █████ ███████████ ░░░███████░      ███████████  █████   █████ █████  ░░█████ █████ ░░████
  ░░░░░░░░░  ░░░░░ ░░░░░░░░░░░    ░░░░░░░       ░░░░░░░░░░░  ░░░░░   ░░░░░ ░░░░░    ░░░░░ ░░░░░   ░░░░ </pre>
                                                                                     
<h2> Welcome to the Gilo Bank API V1 </h2>

<strong><em> About the project </em></strong> 

The Gilo Bank API provides a simple and secure platform for all your banking needs. Whether you're a customer, third party or system administrator. 
As a customer you can easily view your balances, transfer funds, make credits or debits, all with built in fraud monitoring to keep your money safe. 
As a Third Party you have the ability to transfer, credit and debit on your customers behalf. 
Admin's you are <em>ALL POWERFUL</em> the API allows you to do all of the above with the added ability to access all account details, update details, 
or create complete new entities. You're able to create addresses, login details, account holders, third parties, Checking, Student and Savings accounts
as well as our brand new Credit cards. 

All transactions are monitored with our intelligent Fraud system which will instantly be able to freeze funds should your account be at risk. Keeping 
your hard earned money safe. 

The system is built with stabiity and security in mind, customers and third parties are only able to access what they authorised to access meaning your 
details will never end up in the wrong hands. 

For a more detailed look at what routes are available to your please visit localhost:8080/swagger-ui/ 


<strong><em> Features that I am proud of: </em> </strong> 

<ul>
  <li> <b>Transaction Logs</b> All transactions are logged and recorded with the currency, amount date and type. These can then be accessed by customers
    or admins using a date range in the header </li>
  <li> <b> Testing </b> I continually created unit tests and once I added a new feature I would write the tests before moving on helping me to build a 
    robut and reliable API. </li>
  <li> <b> Authentication Checks </b> I found this aspect of the project quite challenging, I originally set up the authentication through SQL but, after 
    reflection I changed my approach. I created a new component that was able to get the Authentication and principle and then return their unique details.</li>
<li><b> Account Creation </b> When passing a JSON body to create a new account, the system will automatically find the existing account holder details, or if 
you are inputting a brand new customer details they will automatically be created including addresses (assuming it meets validation). This allows you to just 
use the ID for existing account holders if they already exist when passing the body in JSON or Swagger. </li>
  </ul>
  
  <strong><em> What would I change? </em> </strong> 
  <ul>
  <li> <b> Transaction Service </b> Although I am pleased with the methods in this class, I feel the class itself does too much, and upon reflection I would
    reorganise where some of these methods are.</li>
  <li> <b> ID types </b> I have used Long for the ID (accountNumbers) which could cause scaling issues. I think String's would have been a better choice. </li>
   </ul>
  I believe the project meets all the requirements and this can be tested with my unit tests, rather than manually having to use postman. To try it out for 
  yourself just download the project and run the GiloBankApplication (Or right click and run the tests). <br> <br>


<strong> // Helpful Tips // </strong>

<ul>
<li> You can create accounts by just passing a "secretKey" and "Primary Holder"  </li>
<li> Addresses can be created with just a House Number, Street, City and PostCode</li>
<li> Account Holders can be created with just a First Name, Surname, Date Of Birth and Primary Address</li>
<li> Login Details Require a Username, Password and User(Admin, Third party or Account Holder)</li>
<li> Accounts are automatically frozen when fraud is detected </li>
</ul>

<strong> // SQL DATA // </strong>

INSERT INTO ADDRESS (house_number, street, city, postcode) VALUES <br>
('38', 'Mere Road', 'Stourbridge', 'DY8 2YY'),<br>
('27', 'Ridge Street', 'Sheffield', 'SF8 7HS'),<br>
('99', 'Wind Road', 'Heron', 'HR1 8HB');<br>
<br>
INSERT INTO account_holder (id, first_name, surname, primary_address, date_of_birth, role) VALUES<br>
(1, 'Nathan', 'Giles', 1, '1988-04-05', "ACCOUNTHOLDER"),<br>
(2, 'Sarah', 'Break', 2, '1990-10-23', "ACCOUNTHOLDER");<br>
   <br>
INSERT INTO third_party (id, name, hashedkey, role) VALUES <br>
(3, 'thirdparty', 'thekey', 'THIRDPARTY');<br>
<br>
INSERT INTO admin (id, name, role) VALUES<br>
(4, 'admin', 'ADMIN');<br>
<br>
INSERT INTO login_details (id, username, password, user_id) VALUES<br>
(1, "hackerman", "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 1),<br>
(2, 'testLogin2', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 2),<br>
(3, 'thirdparty1', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 3), <br>
(4, 'admin1', "$2a$10$MSzkrmfd5ZTipY0XkuCbAejBC9g74MAg2wrkeu8/m1wQGXDihaX3e", 4);<br>

INSERT INTO checking_account (account_number, account_balance, open_date, penalty_amount, status, primary_holder, secondary_holder, checking_min_balance, checking_monthly_maintenance, secret_key, currency_balance) VALUES <br>
(32165487, 2500.00, '2021-05-05', 40, 'ACTIVE', 1, 2, 0, 12, 'secretkey', 'USD'), <br>
(32165488, 2500.00, '2021-05-05', 40, 'ACTIVE', 2, 1, 0, 12, 'secretkey', 'USD'), <br>
(32165489, 2500.00, '2021-05-05', 40, 'ACTIVE', 1, null, 0, 12, 'secretkey', 'USD'), <br>
(32165481, 2500.00, '2021-05-05', 40, 'ACTIVE', 2, null, 0, 12, 'secretkey', 'USD'); <br>

<br>
INSERT INTO transaction (id, transaction_amount, balance_amount, name, time_of_trns, type, account_id) VALUES <br>
-- Account 32165487<br>
-- Day 1 - Total debit = 300.00 <br>
(1, 100.00, 900.00, '250 Debit', '2021-09-17 08:00:00', 'DEBIT' ,32165487),<br>
(2, 200.00, 700.00, '200 Debit', '2021-09-17 09:00:00', 'DEBIT' ,32165487),<br><br>
-- Day 2 - Total Debit = 1200<br>
(3, 300.00, 900.00, '300 Debit', '2021-09-15 08:00:00', 'DEBIT' ,32165487),<br>
(4, 400.00, 500.00, '400 Debit', '2021-09-15 10:00:00', 'DEBIT' ,32165487),<br>
(5, 500.00, 0.00, '500 Debit', '2021-09-15 11:00:00', 'DEBIT' ,32165487),<br><br>
-- Day 3 - Total Debit = 2000<br>
(6, 500.00, 2500.00, '500 Debit', '2021-09-13 06:00:00', 'DEBIT' ,32165487),<br>
(7, 500.00, 2000.00, '500 Debit', '2021-09-13 08:35:20', 'DEBIT' ,32165487),<br>
(8, 500.00, 1500.00, '500 Debit', '2021-09-13 08:00:21', 'DEBIT' ,32165487),<br>
(9, 500.00, 1000.00, '500 Debit', '2021-09-13 12:00:59', 'DEBIT' ,32165487),<br><br>
-- Account 32165488<br>
-- Day 1 - Total debit = 300.00<br>
(10, 100.00, 900.00, '250 Debit', '2021-09-17 08:00:00', 'DEBIT' ,32165488),<br>
(11, 200.00, 700.00, '200 Debit', '2021-09-17 09:00:00', 'DEBIT' ,32165488),<br><br>
-- Day 2 - Total Debit = 1200<br>
(12, 300.00, 900.00, '300 Debit', '2021-09-15 08:00:00', 'DEBIT' ,32165488),<br>
(13, 400.00, 500.00, '400 Debit', '2021-09-15 10:00:00', 'DEBIT' ,32165488),<br>
(14, 500.00, 0.00, '500 Debit', '2021-09-15 11:00:00', 'DEBIT' ,32165488),<br><br>
-- Day 3 - Total Debit = 2000<br>
(15, 500.00, 2500.00, '500 Debit', '2021-09-13 06:00:00', 'DEBIT' ,32165488),<br>
(16, 500.00, 2000.00, '500 Debit', '2021-09-13 08:35:20', 'DEBIT' ,32165488),<br>
(17, 500.00, 1500.00, '500 Debit', '2021-09-13 08:00:21', 'DEBIT' ,32165488),<br>
(18, 500.00, 1000.00, '500 Debit', '2021-09-13 12:00:59', 'DEBIT' ,32165488),<br><br>
-- Account 32165489<br>
-- Day 1 - Total debit = 300.00<br>
(19, 100.00, 900.00, '250 Debit', '2021-09-17 08:00:00', 'DEBIT' ,32165489),<br>
(20, 200.00, 700.00, '200 Debit', '2021-09-17 09:00:00', 'DEBIT' ,32165489),<br><br>
-- Day 2 - Total Debit = 1200<br>
(21, 300.00, 900.00, '300 Debit', '2021-09-15 08:00:00', 'DEBIT' ,32165489),<br>
(22, 400.00, 500.00, '400 Debit', '2021-09-15 10:00:00', 'DEBIT' ,32165489),<br>
(23, 500.00, 0.00, '500 Debit', '2021-09-15 11:00:00', 'DEBIT' ,32165489),<br><br>
-- Day 3 - Total Debit = 2000<br>
(24, 500.00, 2500.00, '500 Debit', '2021-09-13 06:00:00', 'DEBIT' ,32165489),<br>
(25, 500.00, 2000.00, '500 Debit', '2021-09-13 08:35:20', 'DEBIT' ,32165489),<br>
(26, 500.00, 1500.00, '500 Debit', '2021-09-13 08:00:21', 'DEBIT' ,32165489),<br>
(27, 500.00, 1000.00, '500 Debit', '2021-09-13 12:00:59', 'DEBIT' ,32165489),<br><br>
-- Account 32165481<br>
-- Day 1 - Total debit = 300.00<br>
(28, 100.00, 900.00, '250 Debit', '2021-09-17 08:00:00', 'DEBIT' ,32165481),<br>
(29, 200.00, 700.00, '200 Debit', '2021-09-17 09:00:00', 'DEBIT' ,32165481),<br>
-- Day 2 - Total Debit = 1200<br>
(30, 300.00, 900.00, '300 Debit', '2021-09-15 08:00:00', 'DEBIT' ,32165481),<br>
(31, 400.00, 500.00, '400 Debit', '2021-09-15 10:00:00', 'DEBIT' ,32165481),<br>
(32, 500.00, 0.00, '500 Debit', '2021-09-15 11:00:00', 'DEBIT' ,32165481),<br>
-- Day 3 - Total Debit = 2000 <br>
(33, 500.00, 2500.00, '500 Debit', '2021-09-13 06:00:00', 'DEBIT' ,32165481),<br>
(34, 500.00, 2000.00, '500 Debit', '2021-09-13 08:35:20', 'DEBIT' ,32165481),<br>
(35, 500.00, 1500.00, '500 Debit', '2021-09-13 08:00:21', 'DEBIT' ,32165481),<br>
(36, 500.00, 1000.00, '500 Debit', '2021-09-13 12:00:59', 'DEBIT' ,32165481),<br>


