package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Role;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CheckingAccountRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;

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

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));

    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }


    @Test
    void CheckingAccountCreation_TEST_PositiveSingleAccount() throws ParseException {
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        CheckingAccount testAccount = new CheckingAccount("secretKey1", testHolder1, new Money( new BigDecimal("150")));
        checkingAccountRepository.save(testAccount);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeAfter, repoSizeBefore + 1);
        assertEquals("Test1", testAccount.getPrimaryHolder().getFirstName());
        assertEquals(Role.ACCOUNTHOLDER, testAccount.getPrimaryHolder().getRole());
    }

    @Test
    void CheckingAccountCreation_TEST_PositiveJointAccount() throws ParseException {
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        CheckingAccount testAccount = new CheckingAccount("secretKeyJoint", testHolder1, testHolder2,new Money( new BigDecimal("150")));
        checkingAccountRepository.save(testAccount);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeAfter, repoSizeBefore + 1);
        assertEquals("Test1", testAccount.getPrimaryHolder().getFirstName());
        assertEquals("Test2", testAccount.getSecondaryHolder().getFirstName());
    }

    @Test
    void CheckingAccountCreation_TEST_PositiveOnlyHoldersAndBalance() throws ParseException {
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        CheckingAccount testAccount = new CheckingAccount("secretKeyJoint", testHolder1, testHolder2,new Money( new BigDecimal("150")));
        checkingAccountRepository.save(testAccount);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeAfter, repoSizeBefore + 1);
        assertEquals(new BigDecimal("40.00"), testAccount.getPenaltyFee());
        assertEquals(Status.ACTIVE, testAccount.getStatus());
    }

    @Test
    void checkingAccountCreation_Test_ThrowsConstraintViolation() {
        CheckingAccount checkingAccount = new CheckingAccount();
        assertThrows(TransactionSystemException.class, () -> checkingAccountRepository.save(checkingAccount));
    }

    @Test
    void getAccountBalanceByAccountNumber(){
        CheckingAccount testAccount = new CheckingAccount("secretKeyJoint", testHolder1, testHolder2,new Money( new BigDecimal("150.00")));
        checkingAccountRepository.save(testAccount);
        var result = checkingAccountRepository.getBalanceByAccountNumber(testAccount.getAccountNumber());
        assertEquals(new BigDecimal("150.00"), result);
    }

}