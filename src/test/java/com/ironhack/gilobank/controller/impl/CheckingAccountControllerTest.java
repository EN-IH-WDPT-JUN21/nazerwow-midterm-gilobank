package com.ironhack.gilobank.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.gilobank.controller.dto.AccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.repositories.AccountHolderRepository;
import com.ironhack.gilobank.repositories.AddressRepository;
import com.ironhack.gilobank.repositories.CheckingAccountRepository;
import com.ironhack.gilobank.repositories.LoginDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CheckingAccountControllerTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
        LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");

        loginDetails1 = new LoginDetails("hackerman", "ihackthings");
        loginDetails2 = new LoginDetails("testusername2", "testpass2");

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder(loginDetails1, "Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder(loginDetails2, "Test2", "TestSur2", testDateOfBirth2,testAddress2, null);

        testAccount1 = new CheckingAccount(
                "secretKey1",
                testHolder1,                        // Primary Holder
                testHolder2,                        // Secondary Holder
                new BigDecimal("1000.00"),      // balance
                new BigDecimal("10.00"),        // penaltyFee
                LocalDate.parse("2011-01-01"),      // open date
                Status.ACTIVE,                      // Status
                new BigDecimal("11.00"),        // Monthly Maintenance Fee
                new BigDecimal("100.00") );     // Minimum Balance
        testAccount2 = new CheckingAccount(
                "secretKey2",
                testHolder1,                        // Primary Holder
                null,
                new BigDecimal("2000.00"),      // balance
                new BigDecimal("20.00"),        // penaltyFee
                LocalDate.parse("2012-02-02"),      // open date
                Status.ACTIVE,                      // Status
                new BigDecimal("22.00"),        // Monthly Maintenance Fee
                new BigDecimal("200.00") );     // Minimum Balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                        // Primary Holder
                null,
                new BigDecimal("3000.00"),      // balance
                new BigDecimal("30.00"),        // penaltyFee
                LocalDate.parse("2013-03-03"),      // open date
                Status.ACTIVE,                      // Status
                new BigDecimal("33.00"),        // Monthly Maintenance Fee
                new BigDecimal("300.00") );     // Minimum Balance

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1,testHolder2));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void getByAccountNumber_TestValid() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/account/checking/" + testAccount1.getAccountNumber()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getMinimumBalance())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getMinimumBalance())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount3.getMinimumBalance())));
    }

    @Test
    void getAll_TestValid() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/account/checking"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getMinimumBalance())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getMinimumBalance())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount3.getMinimumBalance())));
    }

    @Test
    void creditFunds_Valid() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(), new BigDecimal("250"));
        String body = objectMapper.writeValueAsString(transactionDTO);
        MvcResult result = mockMvc.perform(
                put("/account/checking/credit")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andReturn();
        CheckingAccount updatedAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber()).get();
        assertEquals(testAccount1.getBalance().add(new BigDecimal("250.00")), updatedAccount.getBalance());
    }

    @Test
    void debitFunds_Valid() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("250"), testAccount1.getAccountNumber());
        String body = objectMapper.writeValueAsString(transactionDTO);
        MvcResult result = mockMvc.perform(
                        put("/account/checking/debit")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CheckingAccount updatedAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber()).get();
        assertEquals(testAccount1.getBalance().subtract(new BigDecimal("250.00")), updatedAccount.getBalance());
    }

    @Test
    void transferFunds_Valid() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(), new BigDecimal("250"), testAccount2.getAccountNumber());
        String body = objectMapper.writeValueAsString(transactionDTO);
        MvcResult result = mockMvc.perform(
                        put("/account/checking/transfer")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CheckingAccount debitedAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber()).get();
        CheckingAccount creditedAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber()).get();
        assertEquals(testAccount2.getBalance().subtract(new BigDecimal("250.00")), debitedAccount.getBalance());
        assertEquals(testAccount1.getBalance().add(new BigDecimal("250.00")), creditedAccount.getBalance());
    }

}