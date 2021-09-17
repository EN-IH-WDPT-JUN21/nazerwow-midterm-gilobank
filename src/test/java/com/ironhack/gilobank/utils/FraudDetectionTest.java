package com.ironhack.gilobank.utils;

import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FraudDetectionTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private FraudDetection fraudDetection;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private SavingsAccount testAccount1;
    private Transaction testTransaction1;
    private Transaction testTransaction2;
    private Transaction testTransaction3;
    private Transaction testTransaction4;
    private Transaction testTransaction5;
    private Transaction testTransaction6;
    private Transaction testTransaction7;
    private Transaction testTransaction8;
    private Transaction testTransaction9;

    @BeforeEach
    void setUp() throws ParseException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
        LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");

        loginDetails1 = new LoginDetails("hackerman", "ihackthings");
        loginDetails2 = new LoginDetails("testusername2", "testpass2");

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder(loginDetails1, "Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder(loginDetails2, "Test2", "TestSur2", testDateOfBirth2, testAddress2, null);

        testAccount1 = new SavingsAccount(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new BigDecimal("1000"),     // balance
                new BigDecimal("10"),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("100"),      // Minimum Balance
                new BigDecimal("1"));      // interestRate

        // Total 500 in 1 day
        testTransaction1 = new Transaction(testAccount1, "Test1", new BigDecimal("500.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-01-03T10:15:30"));
        testTransaction2 = new Transaction(testAccount1, "Test2", new BigDecimal("500.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-01-03T10:15:30"));
        // Total 400 in 1 day
        testTransaction3 = new Transaction(testAccount1, "Test2", new BigDecimal("200.00"), testAccount1.getBalance(), TransactionType.TRANSFER_DEBIT, LocalDateTime.parse("2020-04-03T10:15:30"));
        testTransaction4 = new Transaction(testAccount1, "Test3", new BigDecimal("200.00"), testAccount1.getBalance(), TransactionType.TRANSFER_DEBIT, LocalDateTime.parse("2020-04-03T10:15:30"));
        // Total 3000 in 1 day
        testTransaction5 = new Transaction(testAccount1, "Test4", new BigDecimal("1500.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-06-03T10:15:30"));
        testTransaction6 = new Transaction(testAccount1, "Test5", new BigDecimal("1500.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-06-03T10:15:30"));
        // Total 750 in 1 day
        testTransaction7 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-07-03T10:15:30"));
        testTransaction8 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-08-03T10:15:30"));
        testTransaction9 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-09-03T10:15:30"));

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        savingsAccountRepository.save(testAccount1);
        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction7, testTransaction8, testTransaction9));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void current24Spend() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionTotal = fraudDetection.current24Spend(testAccount1, new BigDecimal("250.00"));
        assertEquals(new BigDecimal("1000.00"), transactionTotal);
    }

    @Test
    void getHighestEver24Spend() {
        var transactionTotal = fraudDetection.getHighestEver24Spend(testAccount1);
        assertEquals(new BigDecimal("3000.00"), transactionTotal);
    }

    @Test
    void debitsWithin1SecondLast24Hours_Test_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(1));
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionTotal = fraudDetection.fraudDetector(testAccount1, new BigDecimal("10.00"));
        assertTrue(transactionTotal);
    }

    @Test
    void debitsWithin1SecondLast24Hours_Test_Not_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(2));
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(9));
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionTotal = fraudDetection.fraudDetector(testAccount1, new BigDecimal("10.00"));
        assertFalse(transactionTotal);
    }


    @Test
    void fraudDetector_Test_Fraud_Detected_Due_To_Amount() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(6));
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(9));
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionTotal = fraudDetection.fraudDetector(testAccount1, new BigDecimal("2500000.00"));
        assertTrue(transactionTotal);
    }

    @Test
    void fraudDetector_Test_Fraud_Detected_Due_To_1_Second_Rule() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionTotal = fraudDetection.fraudDetector(testAccount1, new BigDecimal("10.00"));
        assertTrue(transactionTotal);
    }


    @Test
    void fraudDetector_Test_No_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionTotal = fraudDetection.fraudDetector(testAccount1, new BigDecimal("250.00"));
        assertFalse(transactionTotal);
    }

}