package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.security.IAuthenticationFacade;
import com.ironhack.gilobank.service.interfaces.IThirdPartyService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ThirdPartyServiceTest {

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
    private ITransactionService transactionService;
    @Autowired
    private IThirdPartyService thirdPartyService;

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

    private ThirdPartyTransactionDTO thirdPartyTransactionDTO;

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

        loginDetails4 = new LoginDetails("testThirdParty", "testpass", thirdParty);
        loginDetails5 = new LoginDetails("TestThirdParty2", "testpass2", thirdParty2);

        details4 = new CustomUserDetails(loginDetails4);
        details5 = new CustomUserDetails(loginDetails5);

        testAccount1 = new CheckingAccount(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new Money(new BigDecimal("3000")),     // balance
                new Money(new BigDecimal("10")),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("11")),      // monthly maintenance Balance
                new Money(new BigDecimal("100")));      // minimum balance
        testAccount2 = new CheckingAccount(
                "secretKey2",
                testHolder1,                    // Primary Holder
                null,
                new Money(new BigDecimal("2000")),     // balance
                new Money(new BigDecimal("20")),       // penaltyFee
                LocalDate.parse("2012-02-02"),  // open date
                Status.ACTIVE,                  // Status
                new Money( new BigDecimal("22")),      // monthly maintenance
                new Money( new BigDecimal("200")));      // minimum balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new Money(  new BigDecimal("3000")),     // balance
                new Money(  new BigDecimal("30")),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new Money( new BigDecimal("300")),      // monthly maintenance
                        new Money( new BigDecimal("3")));      // minimum balance

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        thirdPartyRepository.saveAll(List.of(thirdParty, thirdParty2));
        loginDetailsRepository.saveAll(List.of(loginDetails4, loginDetails5));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));

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
        checkingAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findById() {
        SecurityContextHolder.getContext().setAuthentication(thirdPartyLogin);
        var result = thirdPartyService.findById(thirdParty.getId());
        assertEquals(thirdParty.getHashedKey(), result.getHashedKey());
    }

    @Test
    void creditAccount() {
        SecurityContextHolder.getContext().setAuthentication(thirdPartyLogin);
        thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                new Money(   new BigDecimal("1000.00")),
                testAccount1.getAccountNumber(),
                testAccount1.getSecretKey(),
                null, null);
        var result = thirdPartyService.creditAccount(thirdParty.getHashedKey(), thirdPartyTransactionDTO);
        assertEquals(testAccount1.getBalance().increaseAmount(new BigDecimal("1000.00")),
                checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance());
    }

    @Test
    void debitAccount() {
        SecurityContextHolder.getContext().setAuthentication(thirdPartyLogin);
        thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                new Money(    new BigDecimal("1000.00")),
                null, null,
                testAccount1.getAccountNumber(),
                testAccount1.getSecretKey());
        var result = thirdPartyService.debitAccount(thirdParty.getHashedKey(), thirdPartyTransactionDTO);
        assertEquals(testAccount1.getBalance().decreaseAmount(new BigDecimal("1000.00")),
                checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance());
    }

    @Test
    void transferBetweenAccounts() {
        SecurityContextHolder.getContext().setAuthentication(thirdPartyLogin);
        thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                new Money(    new BigDecimal("1000.00")),
                testAccount2.getAccountNumber(),
                testAccount2.getSecretKey(),
                testAccount1.getAccountNumber(),
                testAccount1.getSecretKey());
        var result = thirdPartyService.transferBetweenAccounts(thirdParty.getHashedKey(), thirdPartyTransactionDTO);
        assertEquals(testAccount1.getBalance().decreaseAmount(new BigDecimal("1000.00")),
                checkingAccountRepository.findById(testAccount1.getAccountNumber()).get().getBalance());
        assertEquals(testAccount2.getBalance().increaseAmount(new BigDecimal("1000.00")),
                checkingAccountRepository.findById(testAccount2.getAccountNumber()).get().getBalance());
    }

    @Test
    void addThirdParty() {
    }

    @Test
    void updateThirdParty() {
    }

    @Test
    void saveThirdParty() {
    }
}