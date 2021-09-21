package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CheckingAccountServiceTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CheckingAccountService checkingAccountService;


    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private CheckingAccount testAccount1;
    private CheckingAccount testAccount2;
    private CheckingAccount testAccount3;

    @BeforeEach
    void setUp() throws ParseException {
        LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
        LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder("Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder("Test2", "TestSur2", testDateOfBirth2, testAddress2, null);

        loginDetails1 = new LoginDetails("hackerman", "ihackthings", testHolder1);
        loginDetails2 = new LoginDetails("testusername2", "testpass2", testHolder2);

        testAccount1 = new CheckingAccount(
                "secretKey1",
                testHolder1,                        // Primary Holder
                testHolder2,                        // Secondary Holder
                new BigDecimal("1000.00"),      // balance
                new BigDecimal("10.00"),        // penaltyFee
                LocalDate.parse("2011-01-01"),      // open date
                Status.ACTIVE,                      // Status
                new BigDecimal("11.00"),        // Monthly Maintenance Fee
                new BigDecimal("100.00"));     // Minimum Balance
        testAccount2 = new CheckingAccount(
                "secretKey2",
                testHolder1,                        // Primary Holder
                null,
                new BigDecimal("2000.00"),      // balance
                new BigDecimal("20.00"),        // penaltyFee
                LocalDate.parse("2012-02-02"),      // open date
                Status.ACTIVE,                      // Status
                new BigDecimal("22.00"),        // Monthly Maintenance Fee
                new BigDecimal("200.00"));     // Minimum Balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                        // Primary Holder
                null,
                new BigDecimal("3000.00"),      // balance
                new BigDecimal("30.00"),        // penaltyFee
                LocalDate.parse("2013-03-03"),      // open date
                Status.ACTIVE,                      // Status
                new BigDecimal("33.00"),        // Monthly Maintenance Fee
                new BigDecimal("300.00"));     // Minimum Balance

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findByAccountNumber() {
        CheckingAccount checkingAccount = checkingAccountService.findByAccountNumber(testAccount1.getAccountNumber());
        assertEquals(checkingAccount.getSecretKey(), testAccount1.getSecretKey());
    }

    @Test
    void findAll() {
        var listOfAccount = checkingAccountService.findAll();
        assertEquals(3, listOfAccount.size());
    }
}