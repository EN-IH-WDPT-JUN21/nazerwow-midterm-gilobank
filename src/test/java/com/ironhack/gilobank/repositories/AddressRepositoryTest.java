package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.LoginDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AddressRepositoryTest {

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
        loginDetailsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void testAddressExists() {
        Optional<Address> addressOptional = addressRepository.findById(testAddress1.getId());
        assertTrue(addressOptional.isPresent());
    }

    @Test
    void testAddressAssignedCorrectly() {
        assertEquals(testAddress1.getPostcode(), testHolder1.getPrimaryAddress().getPostcode());
    }

    @Test
    void testAddressAssigningMailingAddress() {
        testHolder1.setMailingAddress(testAddress2);
        assertEquals(testAddress2.getPostcode(), testHolder1.getMailingAddress().getPostcode());
    }

    @Test
    void testPrimaryAndMailingCanBeTheSame() {
        testHolder1.setMailingAddress(testAddress1);
        assertEquals(testAddress1.getPostcode(), testHolder1.getMailingAddress().getPostcode());
    }

    @Test
    void testAddressCanBeUsedOnMoreThan1AccountHolder() {
        testHolder2.setPrimaryAddress(testAddress1);
        assertEquals(testHolder1.getPrimaryAddress(), testHolder2.getPrimaryAddress());
    }

}