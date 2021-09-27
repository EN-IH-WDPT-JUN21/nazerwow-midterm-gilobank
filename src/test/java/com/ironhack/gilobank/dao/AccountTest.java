package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private Account testAccount1;

    @BeforeEach
    void setUp() {

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
                new Money(new BigDecimal("1000.00")),      // balance
                new Money(new BigDecimal("10.00")),        // penaltyFee
                LocalDate.parse("2011-01-01"),      // open date
                Status.ACTIVE,                      // Status
                new Money(new BigDecimal("11.00")),        // Monthly Maintenance Fee
                new Money(new BigDecimal("100.00")));     // Minimum Balance
    }

    @Test
    void getBalanceAsMoney() {
        Money testAmount = new Money(new BigDecimal("1000.00"));
        assertEquals(testAmount.getCurrency(), testAccount1.getBalance().getCurrency());
    }

    @Test
    void credit() {
        Money startAmount = new Money(new BigDecimal("1000.00"));
        BigDecimal endAmount = startAmount.increaseAmount(new BigDecimal("250"));
        testAccount1.getBalance().increaseAmount(new BigDecimal("250"));
        assertEquals(endAmount, testAccount1.getBalance().getAmount());
    }

    @Test
    void debit() {
        Money startAmount = new Money(new BigDecimal("1000.00"));
        BigDecimal endAmount = startAmount.decreaseAmount(new BigDecimal("250"));
        testAccount1.getBalance().decreaseAmount(new BigDecimal("250"));
        assertEquals(endAmount, testAccount1.getBalance().getAmount());
    }

    @Test
    void freezeAccount() {
        testAccount1.freezeAccount();
        assertEquals(Status.FROZEN, testAccount1.getStatus());
    }

    @Test
    void unfreezeAccount() {
        testAccount1.freezeAccount();
        assertEquals(Status.FROZEN, testAccount1.getStatus());
        testAccount1.unfreezeAccount();
        assertEquals(Status.ACTIVE, testAccount1.getStatus());
    }

}