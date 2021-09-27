package com.ironhack.gilobank.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.gilobank.controller.dto.CreditCardDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private AdminRepository adminRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private TransactionRepository transactionRepository;

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
    private LoginDetails loginDetails1, loginDetails2, loginDetails3, loginDetails4, loginDetails5;
    private Admin admin;
    private ThirdParty thirdParty, thirdParty2;
    private CustomUserDetails details1, details2, details3, details4, details5;
    private UsernamePasswordAuthenticationToken adminLogin, login1, login2, thirdPartyLogin, thirdPartyLogin2;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate dateNow = LocalDate.now();

    @BeforeEach
    void setUp() throws ParseException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
        thirdPartyRepository.save(thirdParty);
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2, loginDetails3, loginDetails4));
        creditCardRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));

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
        creditCardRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        adminRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void getByAccountNumber_TestValid() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        MvcResult result = mockMvc.perform(
                        get("/api/account/creditcard/" + testAccount1.getAccountNumber()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getCreditLimit().getAmount())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getCreditLimit().getAmount())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount3.getCreditLimit().getAmount())));
    }

    @Test
    void getAll_TestValid() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        MvcResult result = mockMvc.perform(
                        get("/api/account/creditcard/"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getCreditLimit().getAmount())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getCreditLimit().getAmount())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount3.getCreditLimit().getAmount())));
    }

    @Test
    void creditFunds_Valid() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(), new Money(new BigDecimal("250.00")), TransactionType.CREDIT);
        String body = objectMapper.writeValueAsString(transactionDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/account/creditcard/credit")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CreditCard updatedAccount = creditCardRepository.findById(transactionDTO.getCreditAccountNumber()).get();
        assertEquals(testAccount1.getBalance().increaseAmount(new BigDecimal("250.00")), updatedAccount.getBalance().getAmount());
    }

    @Test
    void debitFunds_Valid() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        TransactionDTO transactionDTO = new TransactionDTO(new Money(new BigDecimal("250.00")), testAccount1.getAccountNumber(), TransactionType.DEBIT);
        String body = objectMapper.writeValueAsString(transactionDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/account/creditcard/debit")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CreditCard updatedAccount = creditCardRepository.findById(transactionDTO.getDebitAccountNumber()).get();
        assertEquals(testAccount1.getBalance().decreaseAmount(new BigDecimal("250.00")), updatedAccount.getBalance().getAmount());
    }

    @Test
    void transferFunds_Valid() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(), new Money(new BigDecimal("250")), testAccount2.getAccountNumber());
        String body = objectMapper.writeValueAsString(transactionDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/account/creditcard/transfer")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CreditCard debitedAccount = creditCardRepository.findById(transactionDTO.getDebitAccountNumber()).get();
        CreditCard creditedAccount = creditCardRepository.findById(transactionDTO.getCreditAccountNumber()).get();
        assertEquals(testAccount2.getBalance().decreaseAmount(new BigDecimal("250.00")), debitedAccount.getBalance().getAmount());
        assertEquals(testAccount1.getBalance().increaseAmount(new BigDecimal("250.00")), creditedAccount.getBalance().getAmount());
    }

    @Test
    void getTransactionsByDateBetween() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);

        Transaction testTransaction1 = new Transaction(testAccount1, "Test1", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction2 = new Transaction(testAccount1, "Test2", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));
        Transaction testTransaction3 = new Transaction(testAccount1, "Test2", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-03-03T10:15:30"));
        Transaction testTransaction4 = new Transaction(testAccount1, "Test3", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-04-03T10:15:30"));
        Transaction testTransaction5 = new Transaction(testAccount1, "Test4", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-05-03T10:15:30"));
        Transaction testTransaction6 = new Transaction(testAccount1, "Test5", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-06-03T10:15:30"));
        Transaction testTransaction7 = new Transaction(testAccount1, "Test6", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-07-03T10:15:30"));
        Transaction testTransaction8 = new Transaction(testAccount1, "Test7", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-08-03T10:15:30"));
        Transaction testTransaction9 = new Transaction(testAccount1, "Test8", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-09-03T10:15:30"));

        Transaction testTransaction10 = new Transaction(testAccount2, "Test10", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction11 = new Transaction(testAccount2, "Test11", new Money(new BigDecimal("250.00")), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));

        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction7, testTransaction8, testTransaction9, testTransaction10,
                testTransaction11));

        MvcResult result = mockMvc.perform(
                        get("/api/account/creditcard/"
                                + testAccount1.getAccountNumber()
                                + "/2020-01-03/2020-05-03"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test1"));
        assertTrue(result.getResponse().getContentAsString().contains("Test2"));
        assertTrue(result.getResponse().getContentAsString().contains("Test3"));
        assertTrue(result.getResponse().getContentAsString().contains("Test4"));
        assertFalse(result.getResponse().getContentAsString().contains("Test5"));
        assertFalse(result.getResponse().getContentAsString().contains("Test7"));
        assertFalse(result.getResponse().getContentAsString().contains("Test10"));
        assertFalse(result.getResponse().getContentAsString().contains("Test11"));
    }

    @Test
    void createNewCreditCard() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        var repoSizeBefore = creditCardRepository.findAll().size();
        CreditCardDTO creditCardDTO = new CreditCardDTO("secreKey", testHolder1, testHolder2, new BigDecimal("-200.00"),
                new BigDecimal("40.00"), dateNow, Status.ACTIVE, new BigDecimal("-5000.00"), new BigDecimal(".20"));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        MvcResult result = mockMvc.perform(
                        put("/api/account/creditcard/new")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        var repoSizeAfter = creditCardRepository.findAll().size();
        assertTrue(result.getResponse().getContentAsString().contains("secreKey"));
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void updateCreditCard() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        var repoSizeBefore = creditCardRepository.findAll().size();
        CreditCardDTO creditCardDTO = new CreditCardDTO("secreKey", testHolder1, testHolder2, new BigDecimal("-999.00"),
                new BigDecimal("40.00"), dateNow, Status.ACTIVE, new BigDecimal("-5000.00"), new BigDecimal(".20"));
        String body = objectMapper.writeValueAsString(creditCardDTO);

        MvcResult result = mockMvc.perform(
                        put("/api/account/creditcard/" + testAccount1.getAccountNumber() + "/update")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("secretKey"));
        var repoSizeAfter = creditCardRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
        assertEquals(new BigDecimal("-999.00"), creditCardRepository.findById(testAccount1.getAccountNumber()).get().getBalance().getAmount());
    }

    @Test
    void getByBalance_TestValid() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        MvcResult result = mockMvc.perform(
                        get("/api/account/creditcard/" + testAccount1.getAccountNumber() + "/balance"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getBalance().getAmount())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getAccountNumber())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getSecretKey())));
    }

    @Test
    void getByBalance_Test_AccountOwner() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(login1);
        MvcResult result = mockMvc.perform(
                        get("/api/account/creditcard/" + testAccount1.getAccountNumber() + "/balance"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getBalance().getAmount())));
        assertTrue(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getAccountNumber())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount1.getSecretKey())));
    }

    @Test
    void getByBalance_Test_NotAllowed() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(login2);
        MvcResult result = mockMvc.perform(
                        get("/api/account/creditcard/" + testAccount2.getAccountNumber() + "/balance"))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getBalance())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getAccountNumber())));
        assertFalse(result.getResponse().getContentAsString().contains(String.valueOf(testAccount2.getSecretKey())));
    }
}

