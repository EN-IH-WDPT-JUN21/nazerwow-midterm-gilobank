package com.ironhack.gilobank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.service.interfaces.ICreditCardService;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditCardServiceTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ICreditCardService creditCardService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private CreditCard testAccount1;
    private CreditCard testAccount2;
    private CreditCard testAccount3;
    private LoginDetails  loginDetails3;
    private Admin admin;
    private CustomUserDetails details3;
    private UsernamePasswordAuthenticationToken adminLogin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate dateNow = LocalDate.now();

    @BeforeEach
    void setUp() throws ParseException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder("Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder("Test2", "TestSur2", testDateOfBirth2, testAddress2, null);
        admin = new Admin("Admin");

        loginDetails3 = new LoginDetails("testAdmin", "testpass", admin);

        details3 = new CustomUserDetails(loginDetails3);


        testAccount1 = new CreditCard(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new Money(new BigDecimal("-500.00")),      // balance
                new Money(new BigDecimal("10")),       // penaltyFee
                LocalDate.now(),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("-1000.00")),     // Credit Limit Balance
                new BigDecimal("0.1"));     // interestRate
        testAccount2 = new CreditCard(
                "secretKey2",
                testHolder1,                    // Primary Holder
                null,
                new Money(new BigDecimal("-500.00")),      // balance
                new Money(new BigDecimal("20")),       // penaltyFee
                LocalDate.now(),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("-2000.00")),     // Credit Limit Balance
                new BigDecimal("0.1"));     // Interest Rate
        testAccount3 = new CreditCard(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new Money(new BigDecimal("-500.00")),      // balance
                new Money(new BigDecimal("30")),       // penaltyFee
                LocalDate.now(),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("-3000.00")),     // Credit Limit Balance
                new BigDecimal("0.1"));     // Interest Rate

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        adminRepository.save(admin);
        loginDetailsRepository.saveAll(List.of(loginDetails3));
        creditCardRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));

        adminLogin = new UsernamePasswordAuthenticationToken(details3, "z",
                singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(adminLogin);

        SecurityContextHolder.getContext().setAuthentication(adminLogin);
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        adminRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void availableFunds_true() {
        TransactionDTO transactionDTO = new TransactionDTO(new Money(new BigDecimal("250.00")), testAccount1.getAccountNumber(), TransactionType.DEBIT);
        var result = creditCardService.availableFunds(transactionDTO);
        assertTrue(result);
    }

    @Test
    void availableFunds_false() {
        TransactionDTO transactionDTO = new TransactionDTO(new Money(new BigDecimal("900000.00")), testAccount1.getAccountNumber(), TransactionType.DEBIT);
        var result = creditCardService.availableFunds(transactionDTO);
        assertFalse(result);
    }
}