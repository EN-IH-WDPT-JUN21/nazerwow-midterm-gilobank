package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Role;
import com.ironhack.gilobank.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    private Admin admin1;

    private UsernamePasswordAuthenticationToken login1, login2, login3;

    @BeforeEach
    void setUp() throws ParseException {
        LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
        LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder("Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder("Test2", "TestSur2", testDateOfBirth2, testAddress2, null);

        admin1 = new Admin("admin1");

        loginDetails1 = new LoginDetails("hackerman", "ihackthings", testHolder1);
        loginDetails2 = new LoginDetails("testusername2", "testpass2", testHolder2);

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));

        login1 = new UsernamePasswordAuthenticationToken(loginDetails1, "x");
        login2 = new UsernamePasswordAuthenticationToken(admin1, "z",
                singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        login3 = new UsernamePasswordAuthenticationToken(loginDetails2, "z",
                singleton(new SimpleGrantedAuthority("ROLE_ACCOUNTHOLDER")));
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findAll_secure() {
        CheckingAccount testAccount = new CheckingAccount("secretKey1", testHolder1, null, new BigDecimal("150"));
        CheckingAccount testAccount3 = new CheckingAccount("secretKey3", testHolder1, testHolder2, new BigDecimal("650"));
        CheckingAccount testAccount2 = new CheckingAccount("secretKey2", testHolder2, null, new BigDecimal("350"));
        checkingAccountRepository.save(testAccount);
        checkingAccountRepository.save(testAccount2);
        checkingAccountRepository.save(testAccount3);

        SecurityContextHolder.getContext().setAuthentication(login1);
        var checkingAccounts = checkingAccountRepository.findAllSecure();
        assertEquals(2, checkingAccounts.size());
        SecurityContextHolder.getContext().setAuthentication(login2);
        var checkingAccounts2 = checkingAccountRepository.findAllSecure();
        assertEquals(3, checkingAccounts2.size());
        SecurityContextHolder.getContext().setAuthentication(login3);
        var checkingAccount3 = checkingAccountRepository.findAllSecure();
        assertEquals(1, checkingAccount3.size());
        assertEquals("secretKey2", checkingAccount3.get(0).getSecretKey());
    }

    @Test
    void findByAccountNumber_secure() {
        CheckingAccount testAccount = new CheckingAccount("secretKey1", testHolder1, new BigDecimal("150"));
        CheckingAccount testAccount3 = new CheckingAccount("secretKey3", testHolder1, new BigDecimal("650"));
        CheckingAccount testAccount2 = new CheckingAccount("secretKey2", testHolder2, new BigDecimal("350"));
        checkingAccountRepository.save(testAccount);
        checkingAccountRepository.save(testAccount2);
        checkingAccountRepository.save(testAccount3);

        SecurityContextHolder.getContext().setAuthentication(login1);
        var checkingAccounts = checkingAccountRepository.findByAccountNumberSecure(testAccount.getAccountNumber());
        assertEquals(testAccount.getSecretKey(), checkingAccounts.get().getSecretKey());
        SecurityContextHolder.getContext().setAuthentication(login3);
        var checkingAccounts2 = checkingAccountRepository.findByAccountNumberSecure(testAccount.getAccountNumber());
        assertFalse(checkingAccounts2.isPresent());
        SecurityContextHolder.getContext().setAuthentication(login2);
        var checkingAccounts3 = checkingAccountRepository.findByAccountNumberSecure(testAccount.getAccountNumber());
        assertEquals(testAccount.getSecretKey(), checkingAccounts3.get().getSecretKey());
    }

    @Test
    void CheckingAccountCreation_TEST_PositiveSingleAccount() throws ParseException {
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        CheckingAccount testAccount = new CheckingAccount("secretKey1", testHolder1, new BigDecimal("150"));
        checkingAccountRepository.save(testAccount);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeAfter, repoSizeBefore + 1);
        assertEquals("Test1", testAccount.getPrimaryHolder().getFirstName());
        assertEquals(Role.ACCOUNTHOLDER, testAccount.getPrimaryHolder().getRole());
    }

    @Test
    void CheckingAccountCreation_TEST_PositiveJointAccount() throws ParseException {
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        CheckingAccount testAccount = new CheckingAccount("secretKeyJoint", testHolder1, testHolder2, new BigDecimal("150"));
        checkingAccountRepository.save(testAccount);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeAfter, repoSizeBefore + 1);
        assertEquals("Test1", testAccount.getPrimaryHolder().getFirstName());
        assertEquals("Test2", testAccount.getSecondaryHolder().getFirstName());
    }

    @Test
    void CheckingAccountCreation_TEST_PositiveOnlyHoldersAndBalance() throws ParseException {
        var repoSizeBefore = checkingAccountRepository.findAll().size();
        CheckingAccount testAccount = new CheckingAccount("secretKeyJoint", testHolder1, testHolder2, new BigDecimal("150"));
        checkingAccountRepository.save(testAccount);
        var repoSizeAfter = checkingAccountRepository.findAll().size();
        assertEquals(repoSizeAfter, repoSizeBefore + 1);
        assertEquals(new BigDecimal("40.00"), testAccount.getPenaltyFee());
        assertEquals(Status.ACTIVE, testAccount.getStatus());
    }

}