package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.*;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Role;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.service.interfaces.ICreationService;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreationServiceTest {

    @Autowired
    private ICreationService creationService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private AddressDTO addressDTO;
    private AccountHolderDTO accountHolderDTO;
    private LoginDetailsDTO loginDetailsDTO;
    private CheckingAccountDTO checkingAccountDTO;
    private CreditCardDTO creditCardDTO;
    private SavingsAccountDTO savingsAccountDTO;

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private CheckingAccount testAccount1;
    private CheckingAccount testAccount2;
    private StudentAccount testAccount3;

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
        testAccount3 = new StudentAccount(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new Money(new BigDecimal("3000")),     // balance
                new Money(new BigDecimal("30")),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE);               // Status

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2));
        studentAccountRepository.save(testAccount3);
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void newAddress_BrandNew() {
        addressDTO = new AddressDTO("13", "13", "Fake Street",
                "Fake Town", "Fake City", "pcode");
        var repoSizeBefore = addressRepository.findAll().size();
        creationService.newAddress(addressDTO);
        var repoSizeAfter = addressRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newAddress_UpdateExisting() {
        addressDTO = new AddressDTO(testAddress1.getId(), "13", "13", "Fake Street",
                "Fake Town", "Fake City", "pcode");
        var repoSizeBefore = addressRepository.findAll().size();
        creationService.newAddress(addressDTO);
        var repoSizeAfter = addressRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
        assertEquals("pcode", addressRepository.findById(testAddress1.getId()).get().getPostcode());
    }

    @Test
    void newAccountHolder() {
        accountHolderDTO = new AccountHolderDTO("firstName", "surname", LocalDate.parse("1988-01-01"),
                testAddress1, testAddress2);
        var repoSizeBefore = accountHolderRepository.findAll().size();
        creationService.newAccountHolder(accountHolderDTO);
        var repoSizeAfter = accountHolderRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newAccountHolder_UpdateExisting() {
        accountHolderDTO = new AccountHolderDTO(testHolder1.getId(), "firstName", "surname",
                LocalDate.parse("1988-01-01"),
                testAddress1, testAddress2, Role.ACCOUNTHOLDER);
        var repoSizeBefore = accountHolderRepository.findAll().size();
        creationService.newAccountHolder(accountHolderDTO);
        var repoSizeAfter = accountHolderRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
        assertEquals("firstName", accountHolderRepository.findById(testHolder1.getId()).get().getFirstName());
    }

    @Test
    void addThirdParty() {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setHashedKey("hashedkey232");
        thirdPartyDTO.setName("newThirdParty");
        var result = creationService.newThirdParty(thirdPartyDTO);
        assertTrue(thirdPartyRepository.findByHashedKey("hashedkey232").isPresent());
    }

    @Test
    void updateThirdParty() {
        ThirdParty thirdParty = new ThirdParty("name", "name");
        thirdPartyRepository.save(thirdParty);
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setId(thirdParty.getId());
        thirdPartyDTO.setHashedKey("hashedkey6969");
        thirdPartyDTO.setName("newThirdParty");
        var result = creationService.newThirdParty(thirdPartyDTO);
        assertEquals("hashedkey6969", thirdPartyRepository.findById(thirdParty.getId()).get().getHashedKey());
    }

    @Test
    void newLoginDetails() {
        AccountHolder newHolder = new AccountHolder("name1", "name2", LocalDate.parse("1988-01-01"), testAddress1);
        accountHolderRepository.save(newHolder);
        loginDetailsDTO = new LoginDetailsDTO("username", "password", newHolder);
        var repoSizeBefore = loginDetailsRepository.findAll().size();
        creationService.newLoginDetails(loginDetailsDTO);
        var repoSizeAfter = loginDetailsRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newLoginDetails_UpdateExisting() {
        AccountHolder newHolder = new AccountHolder("name1", "name2", LocalDate.parse("1988-01-01"), testAddress1);
        accountHolderRepository.save(newHolder);
        loginDetailsDTO = new LoginDetailsDTO(loginDetails1.getId(), "username", "password", newHolder);
        var repoSizeBefore = loginDetailsRepository.findAll().size();
        creationService.newLoginDetails(loginDetailsDTO);
        var repoSizeAfter = loginDetailsRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
        assertEquals("username", loginDetailsRepository.findById(loginDetails1.getId()).get().getUsername());
    }

    @Test
    void newCheckingAccount() {
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO("secretKey", testHolder1, testHolder2, new BigDecimal("200.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        creationService.newCheckingAccount(checkingAccountDTO);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newCheckingAccount_UpdateExisting() {
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO(testAccount1.getAccountNumber(), "secretKey", testHolder1, testHolder2, new BigDecimal("200.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        creationService.newCheckingAccount(checkingAccountDTO);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
    }

    @Test
    void newStudentAccount() {
        testHolder1.setDateOfBirth(LocalDate.now());
        testHolder2.setDateOfBirth(LocalDate.now());
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO("secreKey", testHolder1, testHolder2, new BigDecimal("999.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        var repoSizeBefore = studentAccountRepository.findAll().size();
        creationService.newStudentAccount(checkingAccountDTO);
        var repoSizeAfter = studentAccountRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newStudentAccount_UpdateExisting() {
        testHolder1.setDateOfBirth(LocalDate.now());
        testHolder2.setDateOfBirth(LocalDate.now());
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO(testAccount3.getAccountNumber(), "secreKey", testHolder1, testHolder2, new BigDecimal("999.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        var repoSizeBefore = studentAccountRepository.findAll().size();
        creationService.newStudentAccount(checkingAccountDTO);
        var repoSizeAfter = studentAccountRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
    }

    @Test
    void newCreditCard() {
        creditCardDTO = new CreditCardDTO("secreKey", testHolder1, testHolder2, new BigDecimal("-200.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("-2000.00"), new BigDecimal("0.20"));
        var repoSizeBefore = creditCardRepository.findAll().size();
        creationService.newCreditCard(creditCardDTO);
        var repoSizeAfter = creditCardRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newCreditCard_UpdateExisting() {
        CreditCard creditCard = new CreditCard("secretKey1", testHolder1, new Money(new BigDecimal("-100.00")));
        creditCardRepository.save(creditCard);
        creditCardDTO = new CreditCardDTO(creditCard.getAccountNumber(), "secreKey", testHolder1, testHolder2, new BigDecimal("-200.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("-2000.00"), new BigDecimal("0.20"));
        var repoSizeBefore = creditCardRepository.findAll().size();
        creationService.newCreditCard(creditCardDTO);
        var repoSizeAfter = creditCardRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
    }

    @Test
    void newSavingsAccount() {
        savingsAccountDTO = new SavingsAccountDTO("secretKey", testHolder1, testHolder2, new BigDecimal("2000.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("200.00"), new BigDecimal("0.20"));
        var repoSizeBefore = savingsAccountRepository.findAll().size();
        creationService.newSavingsAccount(savingsAccountDTO);
        var repoSizeAfter = savingsAccountRepository.findAll().size();
        assertEquals(repoSizeBefore + 1, repoSizeAfter);
    }

    @Test
    void newSavingsAccount_UpdateExisting() {
        SavingsAccount savingsAccount = new SavingsAccount("secretKey1", testHolder1, new Money(new BigDecimal("1000.00")));
        savingsAccountRepository.save(savingsAccount);
        SavingsAccountDTO savingsAccountDTO = new SavingsAccountDTO(savingsAccount.getAccountNumber(), "secretKey", testHolder1, testHolder2, new BigDecimal("2000.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("200.00"), new BigDecimal("0.20"));
        var repoSizeBefore = savingsAccountRepository.findAll().size();
        creationService.newSavingsAccount(savingsAccountDTO);
        var repoSizeAfter = savingsAccountRepository.findAll().size();
        assertEquals(repoSizeBefore, repoSizeAfter);
    }


    @Test
    void checkIfOver24() {
        testHolder2.setDateOfBirth(LocalDate.now());
        assertTrue(creationService.checkIfOver24(testHolder1));
        assertFalse(creationService.checkIfOver24(testHolder2));
    }

    @Test
    void findOrCreateHolder_CreatedNewHolder() {
        AccountHolder accountHolder = new AccountHolder("testFirstName", "testSurname", LocalDate.parse("1990-01-01"), testAddress1, null);
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setPrimaryHolder(accountHolder);
        checkingAccountDTO.setSecretKey("secretKey");
        checkingAccountDTO.setBalance(new BigDecimal("250.00"));
        var repoBefore = accountHolderRepository.findAll().size();
        creationService.findOrCreateHolder(checkingAccountDTO.getPrimaryHolder());
        var repoAfter = accountHolderRepository.findAll().size();
        assertEquals(repoBefore + 1, repoAfter);
    }

    @Test
    void findOrCreateHolder_FoundExistingHolder() {
        AccountHolder accountHolder = new AccountHolder( "testFirstName", "testSurname", LocalDate.parse("1990-01-01"), testAddress1, null);
        accountHolder.setId(testHolder1.getId());
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setPrimaryHolder(accountHolder);
        checkingAccountDTO.setSecretKey("secretKey");
        checkingAccountDTO.setBalance(new BigDecimal("250.00"));
        var repoBefore = accountHolderRepository.findAll().size();
        creationService.findOrCreateHolder(checkingAccountDTO.getPrimaryHolder());
        var repoAfter = accountHolderRepository.findAll().size();
        assertEquals(repoBefore, repoAfter);
    }

    @Test
    void findOrCreateAddress_CreatedNewAddress() {
        Address address = new Address();
        address.setHouseNumber("20");
        address.setStreet("The Street");
        address.setCity("The City");
        address.setPostcode("pcode");
        testHolder1.setPrimaryAddress(address);
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setPrimaryHolder(testHolder1);
        var repoBefore = addressRepository.findAll().size();
        creationService.findOrCreateAddress(checkingAccountDTO.getPrimaryHolder().getPrimaryAddress());
        var repoAfter = addressRepository.findAll().size();
        assertEquals(repoBefore + 1, repoAfter);
    }

    @Test
    void findOrCreateAddress_FoundExistingAddress() {
        Address address = new Address();
        address.setHouseNumber("20");
        address.setStreet("The Street");
        address.setCity("The City");
        address.setPostcode("pcode");
        address.setId(testAddress1.getId());
        testHolder1.setPrimaryAddress(address);
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setPrimaryHolder(testHolder1);
        var repoBefore = addressRepository.findAll().size();
        creationService.findOrCreateAddress(checkingAccountDTO.getPrimaryHolder().getPrimaryAddress());
        var repoAfter = addressRepository.findAll().size();
        assertEquals(repoBefore, repoAfter);
    }


    @Test
    void findOrCreateLoginDetails_CreateNewLogin() {
        AccountHolder accountHolder = new AccountHolder( "testFirstName", "testSurname", LocalDate.parse("1990-01-01"), testAddress1, null);
        accountHolderRepository.save(accountHolder);
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername("theUserName");
        loginDetails.setPassword("thePassword");
        loginDetails.setUser(accountHolder);
        accountHolder.setLoginDetails(loginDetails);
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
        checkingAccountDTO.setPrimaryHolder(accountHolder);
        var repoBefore = loginDetailsRepository.findAll().size();
        creationService.findOrCreateLoginDetails(checkingAccountDTO.getPrimaryHolder().getLoginDetails());
        var repoAfter = loginDetailsRepository.findAll().size();
        assertEquals(repoBefore + 1, repoAfter);
    }

    @Test
    void findOrCreateLoginDetails_UpdateLogin() {
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setId(loginDetails.getId());
        loginDetails.setUsername("theUserName");
        loginDetails.setPassword("thePassword");
        loginDetails.setUser(testHolder1);
        loginDetails.setId(loginDetails1.getId());
        var repoBefore = loginDetailsRepository.findAll().size();
        creationService.findOrCreateLoginDetails(loginDetails);
        var repoAfter = loginDetailsRepository.findAll().size();
        assertEquals(repoBefore, repoAfter);
    }


    @Test
    void studentOrChecking_New_Checking() {
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO( "secreKey", testHolder1, testHolder2, new BigDecimal("999.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        var repoBefore = checkingAccountRepository.findAll().size();
        creationService.newStudentOrChecking(checkingAccountDTO);
        var repoAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoBefore + 1, repoAfter);
        var repoBefore2 = studentAccountRepository.findAll().size();
        creationService.newStudentOrChecking(checkingAccountDTO);
        var repoAfter2 = studentAccountRepository.findAll().size();
        assertEquals(repoBefore2, repoAfter2);
    }

    @Test
    void studentOrChecking_New_Student() {
        testHolder1.setDateOfBirth(LocalDate.now());
        testHolder2.setDateOfBirth(LocalDate.now());
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO( "secreKey", testHolder1, testHolder2, new BigDecimal("999.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        var repoBefore = checkingAccountRepository.findAll().size();
        creationService.newStudentOrChecking(checkingAccountDTO);
        var repoAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoBefore, repoAfter);
        var repoBefore2 = studentAccountRepository.findAll().size();
        creationService.newStudentOrChecking(checkingAccountDTO);
        var repoAfter2 = studentAccountRepository.findAll().size();
        assertEquals(repoBefore2 + 1, repoAfter2);
    }

    @Test
    void studentOrChecking_New_Throws_Exeption() {
        testHolder1.setDateOfBirth(LocalDate.now());
        accountHolderRepository.saveAll(List.of(testHolder1));
        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO( "secreKey", testHolder1, testHolder2, new BigDecimal("999.00"),
                new BigDecimal("40.00"), LocalDate.now(), Status.ACTIVE, new BigDecimal("12.00"), new BigDecimal("100.00"));
        assertThrows(ResponseStatusException.class, () -> creationService.newStudentOrChecking(checkingAccountDTO));
    }
}