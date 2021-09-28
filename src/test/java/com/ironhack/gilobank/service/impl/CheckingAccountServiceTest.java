package com.ironhack.gilobank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
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
    private AdminRepository adminRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private ICheckingAccountService checkingAccountService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1, loginDetails2, loginDetails3, loginDetails4, loginDetails5;
    private CheckingAccount testAccount1, testAccount2, testAccount3;
    private Admin admin;
    private ThirdParty thirdParty, thirdParty2;
    private CustomUserDetails details1, details2, details3, details4, details5;
    private UsernamePasswordAuthenticationToken adminLogin, login1, login2, thirdPartyLogin, thirdPartyLogin2;

    @BeforeEach
    void setUp() throws ParseException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
                new Money(new BigDecimal("1000")),     // balance
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
                new Money(new BigDecimal("22")),      // monthly maintenance
                new Money(new BigDecimal("200")));      // minimum balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new Money(new BigDecimal("3000")),     // balance
                new Money(new BigDecimal("30")),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("300")),      // monthly maintenance
                new Money(new BigDecimal("300")));      // minimum balance

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        adminRepository.save(admin);
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
        loginDetailsRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findByAccountNumber_ValidOwner() {
        SecurityContextHolder.getContext().setAuthentication(login1);
        CheckingAccount checkingAccount = checkingAccountService.findByAccountNumber(testAccount2.getAccountNumber());
        assertEquals(checkingAccount.getSecretKey(), testAccount2.getSecretKey());
    }

    @Test
    void findByAccountNumber_ValidAdmin() {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        CheckingAccount checkingAccount = checkingAccountService.findByAccountNumber(testAccount1.getAccountNumber());
        assertEquals(checkingAccount.getSecretKey(), testAccount1.getSecretKey());
    }

    @Test
    void findByAccountNumber_InvalidOwner_ThrowsException() {
        SecurityContextHolder.getContext().setAuthentication(login2);
        assertThrows(ResponseStatusException.class, () ->
                checkingAccountService.findByAccountNumber(testAccount2.getAccountNumber()));
    }

    @Test
    void findAll() {
        var listOfAccount = checkingAccountService.findAll();
        assertEquals(3, listOfAccount.size());
    }
}