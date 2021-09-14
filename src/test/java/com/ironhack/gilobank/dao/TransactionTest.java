package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionTest {


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
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private SavingsAccount testAccount1;
    private SavingsAccount testAccount2;
    private SavingsAccount testAccount3;
    private Transaction testTransaction1;
    private Transaction testTransaction2;

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
        testHolder2 = new AccountHolder(loginDetails2, "Test2", "TestSur2", testDateOfBirth2,testAddress2, null);

        testAccount1 = new SavingsAccount(
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new BigDecimal("1000"),     // balance
                new BigDecimal("10"),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("100"),       // Minimum Balance
                new BigDecimal("1") );    // interestRate
        testAccount2 = new SavingsAccount(
                testHolder1,                    // Primary Holder
                new BigDecimal("2000"),     // balance
                new BigDecimal("20"),       // penaltyFee
                LocalDate.parse("2012-02-02"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("200"),       // Minimum Balance
                new BigDecimal("2") );    // Interest Rate
        testAccount3 = new SavingsAccount(
                testHolder2,                    // Primary Holder
                new BigDecimal("3000"),     // balance
                new BigDecimal("30"),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("300"),       // Minimum Balance
                new BigDecimal("3") );    // Interest Rate

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1,testHolder2));
        savingsAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));
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
            void testTransactionIdCreation(){
    testTransaction1 = new Transaction(testAccount1);
    transactionRepository.save(testTransaction1);
    testTransaction2 = new Transaction(testAccount1);
    transactionRepository.save(testTransaction2);
    }

}