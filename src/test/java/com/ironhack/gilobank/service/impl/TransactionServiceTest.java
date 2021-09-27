package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.security.IAuthenticationFacade;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TransactionServiceTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;


    private MockMvc mockMvc;

    private Address testAddress1, testAddress2;
    private AccountHolder testHolder1, testHolder2;
    private LoginDetails loginDetails1, loginDetails2, loginDetails3, loginDetails4, loginDetails5;
    private CheckingAccount testAccount1, testAccount2, testAccount3;
    private Admin admin;
    private ThirdParty thirdParty, thirdParty2;
    private CustomUserDetails details1, details2, details3, details4, details5;
    private UsernamePasswordAuthenticationToken adminLogin, login1, login2, thirdPartyLogin, thirdPartyLogin2;

    @BeforeEach
    void setUp() throws ParseException {

        LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
        LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");


        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder("Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder("Test2", "TestSur2", testDateOfBirth2, testAddress2, null);
        admin = new Admin("Admin");
        thirdParty = new ThirdParty("ThirdParty", "hashedKey");
        thirdParty2 = new ThirdParty("ThirdParty2", "haskedKey2");

        loginDetails1 = new LoginDetails("hackerman", "ihackthings", testHolder1);
        loginDetails2 = new LoginDetails("testusername2", "testpass2", testHolder2);
        loginDetails3 = new LoginDetails("testAdmin", "testpass", admin);
        loginDetails4 = new LoginDetails("testThirdParty", "testpass", thirdParty);
        loginDetails5 = new LoginDetails("TestThirdParty2", "testpass2", thirdParty2);

        details1 = new CustomUserDetails(loginDetails1);
        details2 = new CustomUserDetails(loginDetails2);
        details3 = new CustomUserDetails(loginDetails3);
        details4 = new CustomUserDetails(loginDetails4);
        details5 = new CustomUserDetails(loginDetails5);

        testAccount1 = new CheckingAccount(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new Money( new BigDecimal("1000")),     // balance
                new Money( new BigDecimal("10")),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new Money(   new BigDecimal("11")),      // monthly maintenance Balance
                new Money(   new BigDecimal("100")));      // minimum balance
        testAccount2 = new CheckingAccount(
                "secretKey2",
                testHolder1,                    // Primary Holder
                null,
                new Money(    new BigDecimal("2000")),     // balance
                new Money(    new BigDecimal("20")),       // penaltyFee
                LocalDate.parse("2012-02-02"),  // open date
                Status.ACTIVE,                  // Status
                new Money(   new BigDecimal("22")),      // monthly maintenance
                new Money(   new BigDecimal("200")));      // minimum balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new Money(     new BigDecimal("3000")),     // balance
                new Money(      new BigDecimal("30")),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new Money(     new BigDecimal("300")),      // monthly maintenance
                new Money(    new BigDecimal("300")));      // minimum balance

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        thirdPartyRepository.save(thirdParty);
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2, loginDetails4));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));

        login1 = new UsernamePasswordAuthenticationToken(details1, "x");
        login2 = new UsernamePasswordAuthenticationToken(details2, "x");
        adminLogin = new UsernamePasswordAuthenticationToken(details3, "z",
                singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        thirdPartyLogin = new UsernamePasswordAuthenticationToken(details4, "z",
                singleton(new SimpleGrantedAuthority("ROLE_THIRDPARTY")));
        thirdPartyLogin2 = new UsernamePasswordAuthenticationToken(details5, "z",
                singleton(new SimpleGrantedAuthority("ROLE_THIRDPARTY")));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        studentAccountRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void createTransactionLogCredit_TestValidCreation() {
        TransactionDTO transactionDTO = new TransactionDTO(null,new Money(new BigDecimal("250.00")),
                null,
                LocalDateTime.parse("2012-01-01T10:15:30"),
                TransactionType.CREDIT);
        var sizeBefore = transactionRepository.findAll().size();
        transactionService.createTransactionLog(testAccount1, transactionDTO);
        var sizeAfter = transactionRepository.findAll().size();
        assertEquals(sizeAfter, sizeBefore + 1);
    }

    @Test
    void createTransactionLog_TestDateAutoGenerated() {
        TransactionDTO transactionDTO = new TransactionDTO(null,new Money(new BigDecimal("250.00")),
                null,
                LocalDateTime.parse("2012-01-01T10:15:30"),
                TransactionType.CREDIT);
        Transaction testTransaction = transactionService.createTransactionLog(testAccount1, transactionDTO);
        assertTrue(testTransaction.getTimeOfTrns().toString().contains("2012-01-01T10:15:30"));
    }

    @Test
    void createTransactionLog() {
        TransactionDTO transactionDTO = new TransactionDTO(null,new Money(new BigDecimal("250.00")),
                null,
                LocalDateTime.parse("2012-01-01T10:15:30"),
                TransactionType.CREDIT);
        Transaction testTransaction = transactionService.createTransactionLog(testAccount1, transactionDTO);
        assertEquals(LocalDateTime.parse("2012-01-01T10:15:30"), testTransaction.getTimeOfTrns());
    }


    // Transfer
    @Test
    void createTransactionLogTransfer() {
        Transaction debitTransaction = transactionService.createTransactionLogTransfer(testAccount1, new Money( new BigDecimal("250.00")), testAccount2, LocalDateTime.parse("2012-01-01T10:15:30")).get(0);
        Transaction creditTransaction = transactionService.createTransactionLogTransfer(testAccount1,new Money( new BigDecimal("250.00")), testAccount2, LocalDateTime.parse("2012-01-01T10:15:30")).get(1);
        assertEquals(TransactionType.TRANSFER_DEBIT, debitTransaction.getType());
        assertEquals(TransactionType.TRANSFER_CREDIT, creditTransaction.getType());
    }

    @Test
    void findByDateTimeBetween() {
        Transaction testTransaction1 = new Transaction(testAccount1, "Test1",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction2 = new Transaction(testAccount1, "Test2",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));
        Transaction testTransaction3 = new Transaction(testAccount1, "Test2",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-03-03T10:15:30"));
        Transaction testTransaction4 = new Transaction(testAccount1, "Test3",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-04-03T10:15:30"));
        Transaction testTransaction5 = new Transaction(testAccount1, "Test4",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-05-03T10:15:30"));
        Transaction testTransaction6 = new Transaction(testAccount1, "Test5",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-06-03T10:15:30"));
        Transaction testTransaction7 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-07-03T10:15:30"));
        Transaction testTransaction8 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-08-03T10:15:30"));
        Transaction testTransaction9 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-09-03T10:15:30"));

        Transaction testTransaction10 = new Transaction(testAccount2, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction11 = new Transaction(testAccount2, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));

        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction7, testTransaction8, testTransaction9));

        var transactionList = transactionService.findByDateTimeBetween(testAccount1, LocalDateTime.parse("2020-01-03T10:15:20"), LocalDateTime.parse("2020-04-03T10:30:30"));
        assertEquals(4, transactionList.size());
    }

    @Test
    void creditFunds() {
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(),new Money( new BigDecimal("250")), TransactionType.CREDIT);
        transactionService.creditFunds(transactionDTO);
        var updatedAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber());
        assertEquals(testAccount1.getBalance().increaseAmount(new BigDecimal("250.00")), updatedAccount.get().getBalance().getAmount());
    }

    @Test
    void debitFunds() {
        TransactionDTO transactionDTO = new TransactionDTO(new Money( new BigDecimal("250")), testAccount1.getAccountNumber(), TransactionType.DEBIT);
        transactionService.debitFunds(transactionDTO);
        var updatedAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        assertEquals(testAccount1.getBalance().decreaseAmount(new BigDecimal("250.00")), updatedAccount.get().getBalance().getAmount());
    }

    @Test
    void transferBetweenAccounts() {
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(),new Money( new BigDecimal("250")), testAccount2.getAccountNumber());
        transactionService.transferBetweenAccounts(transactionDTO);
        var updatedDebitedAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        var updatedCreditAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber());
        assertEquals(testAccount2.getBalance().decreaseAmount(new BigDecimal("250.00")), updatedDebitedAccount.get().getBalance().getAmount());
        assertEquals(testAccount1.getBalance().increaseAmount(new BigDecimal("250.00")), updatedCreditAccount.get().getBalance().getAmount());
    }

    @Test
    void findTransactionBetween() {
        // should contain
        Transaction testTransaction1 = new Transaction(testAccount1, "Test1",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction2 = new Transaction(testAccount1, "Test2",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));
        Transaction testTransaction3 = new Transaction(testAccount1, "Test2",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-03-03T10:15:30"));
        Transaction testTransaction4 = new Transaction(testAccount1, "Test3",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-04-03T10:15:30"));
        Transaction testTransaction5 = new Transaction(testAccount1, "Test4",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-05-03T10:15:30"));
        // Will not contain
        Transaction testTransaction6 = new Transaction(testAccount1, "Test5",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-06-03T10:15:30"));
        Transaction testTransaction10 = new Transaction(testAccount2, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));

        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction10));

        var listOfTransactions = transactionService.findTransactionBetween(testAccount1.getAccountNumber(),
                LocalDate.parse("2020-01-03"), LocalDate.parse("2020-05-03"));

        assertEquals(5, listOfTransactions.size());
        assertFalse(listOfTransactions.contains(testTransaction10));
        assertFalse(listOfTransactions.contains(testTransaction6));
    }

    @Test
    void checkForFraud_Test_No_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8", new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));

        TransactionDTO transactionDTO = new TransactionDTO(new Money( new BigDecimal("250.00")), testAccount1.getAccountNumber(), TransactionType.DEBIT);

        assertDoesNotThrow(() -> transactionService.checkForFraud(transactionDTO));
    }

    @Test
    void checkForFraud_Test_1Second_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("10.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("10.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("10.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));


        TransactionDTO transactionDTO = new TransactionDTO(new Money( new BigDecimal("250")), testAccount1.getAccountNumber(), TransactionType.DEBIT);


        assertThrows(ResponseStatusException.class, () -> transactionService.checkForFraud(transactionDTO));
    }

    @Test
    void checkForFraud_Test_24Hour_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));


        TransactionDTO transactionDTO = new TransactionDTO(new Money( new BigDecimal("2500000")), testAccount1.getAccountNumber(), TransactionType.DEBIT);

        assertThrows(ResponseStatusException.class, () -> transactionService.checkForFraud(transactionDTO));
    }

    @Test
    void checkForFraud_Test_Account_Frozen() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));

        TransactionDTO transactionDTO = new TransactionDTO(new Money( new BigDecimal("2500000")), testAccount1.getAccountNumber(), TransactionType.DEBIT);
        Optional<CheckingAccount> optionalCheckingAccount =
                checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        assertEquals(Status.ACTIVE, optionalCheckingAccount.get().getStatus());
        try {
            transactionService.checkForFraud(transactionDTO);
        } catch (ResponseStatusException e) {
            optionalCheckingAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
            assertEquals(Status.FROZEN, optionalCheckingAccount.get().getStatus());
        }
    }


    @Test
    void checkAccountStatus_Active() {
        assertDoesNotThrow(() -> transactionService.checkAccountStatus(testAccount1));
    }

    @Test
    void checkAccountStatus_Frozen() {
        testAccount1.setStatus(Status.FROZEN);
        checkingAccountRepository.save(testAccount1);
        assertThrows(ResponseStatusException.class, () -> transactionService.checkAccountStatus(testAccount1));
    }

    @Test
    void checkAvailableFunds_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> transactionService.checkAvailableFunds(testAccount1, testAccount1.getBalance().getAmount().multiply(new BigDecimal("10"))));
    }

    @Test
    void checkAvailableFunds_DoesNotThrow() {
        assertDoesNotThrow(() -> transactionService.checkAvailableFunds(testAccount1, testAccount1.getBalance().getAmount()));
    }

    @Test
    void penaltyCheck_Deducts_Penalty() {
        testAccount1.setBalance(new Money( new BigDecimal("99.00")));
        var balanceAfterFee = transactionService.penaltyCheck(testAccount1, testAccount1.getMinimumBalance(), testAccount1.getPenaltyFee());
        assertEquals(new BigDecimal("99.00").subtract(testAccount1.getPenaltyFee().getAmount()), balanceAfterFee.getAmount());
    }

    @Test
    void penaltyCheck_Does_Not_Deduct() {
        testAccount1.setBalance(new Money( new BigDecimal("100.00")));
        var balanceAfterFee = transactionService.penaltyCheck(testAccount1, testAccount1.getMinimumBalance(), testAccount1.getPenaltyFee());
        assertEquals(new BigDecimal("100.00"), balanceAfterFee.getAmount());
    }

    @Test
    void findAndReturn_Valid() {
        StudentAccount studentAccount = new StudentAccount("stukey", testHolder1,new Money( new BigDecimal("250.00")));
        SavingsAccount savingsAccount = new SavingsAccount("savkey", testHolder1,new Money( new BigDecimal("500.00")));
        CreditCard creditCard = new CreditCard("cckey", testHolder1,new Money( new BigDecimal("0.00")),new Money( new BigDecimal("-3000.00")), new BigDecimal(".20"));
        studentAccountRepository.save(studentAccount);
        savingsAccountRepository.save(savingsAccount);
        creditCardRepository.save(creditCard);

        var ccFound = transactionService.findAccountTypeAndReturn(creditCard.getAccountNumber());
        assertEquals("cckey", ccFound.getSecretKey());
        var savingFound = transactionService.findAccountTypeAndReturn(savingsAccount.getAccountNumber());
        assertEquals("savkey", savingFound.getSecretKey());
        var stuFound = transactionService.findAccountTypeAndReturn(studentAccount.getAccountNumber());
        assertEquals("stukey", stuFound.getSecretKey());
        var checkFound = transactionService.findAccountTypeAndReturn(testAccount1.getAccountNumber());
        assertEquals("secretKey1", checkFound.getSecretKey());
        assertThrows(ResponseStatusException.class, () -> transactionService.findAccountTypeAndReturn(0L));
    }

    @Test
    void findAccountTypeAndSave() {
        testAccount1.setStatus(Status.FROZEN);
        testAccount1.setBalance(new Money( new BigDecimal("999.99")));

        transactionService.findAccountTypeAndSave(testAccount1);

        var testCase = checkingAccountRepository.findById(testAccount1.getAccountNumber());

        assertEquals(Status.FROZEN, testCase.get().getStatus());
        assertEquals(new BigDecimal("999.99"), testCase.get().getBalance().getAmount());
        assertEquals(testHolder1.getFirstName(), testCase.get().getPrimaryHolder().getFirstName());

    }

    @Test
    void applyInterestYearly_Credit() {
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestYearly(testAccount1.getAccountNumber(),new Money( new BigDecimal("100.00")), new BigDecimal(".10"));
        assertEquals(new BigDecimal("1010.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore + 1);
    }

    @Test
    void applyInterestYearly_Debit() {
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestYearly(testAccount1.getAccountNumber(),new Money( new BigDecimal("-100.00")), new BigDecimal(".10"));
        assertEquals(new BigDecimal("990.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore + 1);
    }

    @Test
    void applyInterestYearly_Zero() {
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestYearly(testAccount1.getAccountNumber(),new Money( new BigDecimal("0.00")), new BigDecimal(".10"));
        assertEquals(new BigDecimal("1000.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore);
    }

    @Test
    void applyInterestYearly_NoInterestDue() {
        testAccount1.setOpenDate(LocalDate.now());
        checkingAccountRepository.save(testAccount1);
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestYearly(testAccount1.getAccountNumber(),new Money( new BigDecimal("0.00")), new BigDecimal(".10"));
        assertEquals(new BigDecimal("1000.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore);
    }

    @Test
    void applyInterestYearly_CheckNotPaidTwice() {
        transactionService.applyInterestYearly(testAccount1.getAccountNumber(),new Money( new BigDecimal("100.00")), new BigDecimal("1.20"));
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestYearly(testAccount1.getAccountNumber(), new Money(new BigDecimal("100.00")), new BigDecimal("1.20"));
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore);
    }

    @Test
    void applyInterestMonthly_Credit() {
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestMonthly(testAccount1.getAccountNumber(), new Money(new BigDecimal("100.00")), new BigDecimal("1.20"));
        assertEquals(new BigDecimal("1010.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore + 1);
    }

    @Test
    void applyInterestMonthly_Debit() {
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestMonthly(testAccount1.getAccountNumber(), new Money(new BigDecimal("-100.00")), new BigDecimal("1.20"));
        assertEquals(new BigDecimal("990.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore + 1);
    }

    @Test
    void applyInterestMonthly_Zero() {
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestMonthly(testAccount1.getAccountNumber(), new Money(new BigDecimal("0.00")), new BigDecimal("1.20"));
        assertEquals(new BigDecimal("1000.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore);
    }

    @Test
    void applyInterestMonthly_NoInterestDue() {
        testAccount1.setOpenDate(LocalDate.now());
        checkingAccountRepository.save(testAccount1);
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestMonthly(testAccount1.getAccountNumber(),new Money( new BigDecimal("0.00")), new BigDecimal("1.20"));
        assertEquals(new BigDecimal("1000.00"), checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore);
    }

    @Test
    void applyInterestMonthly_CheckNotPaidTwice() {
        transactionService.applyInterestMonthly(testAccount1.getAccountNumber(),new Money( new BigDecimal("100.00")), new BigDecimal("1.20"));
        var transBefore = transactionRepository.findAll().size();
        transactionService.applyInterestMonthly(testAccount1.getAccountNumber(),new Money( new BigDecimal("100.00")), new BigDecimal("1.20"));
        var transAfter = transactionRepository.findAll().size();
        assertEquals(transAfter, transBefore);
    }

    @Test
    void checkAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(login1);
        var result = transactionService.checkAuthentication(testAccount1.getAccountNumber());
        assertTrue(result);
        SecurityContextHolder.getContext().setAuthentication(login2);
        assertThrows(ResponseStatusException.class, () ->
                transactionService.checkAuthentication(testAccount2.getAccountNumber()));
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        var result3 = transactionService.checkAuthentication(testAccount1.getAccountNumber());
        assertTrue(result3);
    }

    @Test
    void verifyThirdParty() {
        SecurityContextHolder.getContext().setAuthentication(thirdPartyLogin);
        var result = transactionService.verifyThirdParty(thirdParty.getHashedKey());
        assertTrue(result);
        SecurityContextHolder.getContext().setAuthentication(login2);
        assertThrows(ResponseStatusException.class, () ->
                transactionService.verifyThirdParty("wrongHashedKey"));
        SecurityContextHolder.getContext().setAuthentication(login2);
        assertThrows(ResponseStatusException.class, () ->
                transactionService.verifyThirdParty(thirdParty.getHashedKey()));
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        var result3 = transactionService.verifyThirdParty(thirdParty.getHashedKey());
        assertTrue(result3);
        SecurityContextHolder.getContext().setAuthentication(thirdPartyLogin2);
        var result4 = transactionService.verifyThirdParty(thirdParty.getHashedKey());
        assertFalse(result4);
    }

}