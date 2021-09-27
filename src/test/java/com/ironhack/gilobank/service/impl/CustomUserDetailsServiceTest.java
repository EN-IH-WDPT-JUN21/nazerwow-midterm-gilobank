package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.repositories.AccountHolderRepository;
import com.ironhack.gilobank.repositories.AddressRepository;
import com.ironhack.gilobank.repositories.LoginDetailsRepository;
import com.ironhack.gilobank.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomUserDetailsServiceTest {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;


    CustomUserDetails customUserDetails;
    AccountHolder accountHolder;
    Address testAddress1;
    LoginDetails loginDetails;

    @BeforeEach
    void setUp() {
        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        accountHolder = new AccountHolder("firstName", "surname", LocalDate.parse("1990-01-01"), testAddress1);
        loginDetails = new LoginDetails(1L, "testname", "testpassword", accountHolder);

        addressRepository.save(testAddress1);
        accountHolderRepository.save(accountHolder);
        loginDetailsRepository.save(loginDetails);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadUserByUsername() {
        var result = customUserDetailsService.loadUserByUsername("testname");
        assertEquals("testpassword", result.getPassword());
    }
}