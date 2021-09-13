package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountHolderRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
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

        loginDetails1 = new LoginDetails("hackerman", "ihackthings");
        loginDetails2 = new LoginDetails("testusername2", "testpass2");

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder(loginDetails1, "Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder(loginDetails2, "Test2", "TestSur2", testDateOfBirth2,testAddress2, null);

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1,testHolder2));
    }

    @AfterEach
    void tearDown() {
        accountHolderRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void checkPositiveCreation(){
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(testHolder1.getId());
        assertTrue(accountHolder.isPresent());
    }

    @Test
    void checkPositiveAutoRole(){
        assertEquals(Role.ACCOUNTHOLDER, testHolder1.getRole());
    }

    @Test
    void checkUserName(){
        assertEquals("hackerman", testHolder1.getLoginDetails().getUsername());
    }

    @Test
    void checkAddressAddedCorrectly(){
        assertEquals("Primary Road", testHolder1.getPrimaryAddress().getStreet());
        assertEquals(null, testHolder1.getMailingAddress());
    }
}