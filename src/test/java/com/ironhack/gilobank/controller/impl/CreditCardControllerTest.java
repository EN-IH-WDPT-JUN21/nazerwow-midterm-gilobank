package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CreditCardControllerTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private CreditCard testAccount1;
    private CreditCard testAccount2;
    private CreditCard testAccount3;

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

        testAccount1 = new CreditCard(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new BigDecimal("100"),      // balance
                new BigDecimal("10"),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("1000"),     // Credit Limit Balance
                new BigDecimal("11") );     // interestRate
        testAccount2 = new CreditCard(
                "secretKey2",
                testHolder1,                    // Primary Holder
                null,
                new BigDecimal("200"),      // balance
                new BigDecimal("20"),       // penaltyFee
                LocalDate.parse("2012-02-02"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("2000"),     // Credit Limit Balance
                new BigDecimal("22") );     // Interest Rate
        testAccount3 = new CreditCard(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new BigDecimal("300"),      // balance
                new BigDecimal("30"),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("3000"),     // Credit Limit Balance
                new BigDecimal("33") );     // Interest Rate

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1,testHolder2));
        creditCardRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void getByAccountNumber_TestValid() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/account/creditcard/" + testAccount1.getAccountNumber()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getCreditLimit())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getCreditLimit())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount3.getCreditLimit())));
    }

    @Test
    void getAll_TestValid() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/account/creditcard/"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getCreditLimit())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getCreditLimit())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount3.getCreditLimit())));
    }
}