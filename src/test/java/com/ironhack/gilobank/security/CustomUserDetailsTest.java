package com.ironhack.gilobank.security;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.dao.ThirdParty;
import com.ironhack.gilobank.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomUserDetailsTest {


    CustomUserDetails customUserDetails;

    AccountHolder accountHolder;
    Address testAddress1;
    LoginDetails loginDetails;

    @BeforeEach
    void setUp() {
        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        accountHolder = new AccountHolder("firstName", "surname", LocalDate.parse("1990-01-01"), testAddress1);
        loginDetails = new LoginDetails(1L, "testname", "testpassword", accountHolder);
        customUserDetails = new CustomUserDetails(loginDetails);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getPassword() {
        var password = customUserDetails.getPassword();
        assertEquals("testpassword", password);
    }

    @Test
    void getUsername() {
        var result = customUserDetails.getUsername();
        assertEquals("testname", result);
    }

    @Test
    void getId() {
        var result = customUserDetails.getId();
        assertEquals(1L, result);
    }

    @Test
    void getRole() {
        var result = customUserDetails.getRole();
        assertEquals(Role.ACCOUNTHOLDER, result);
    }

    @Test
    void getHashedKey() {
        ThirdParty thirdParty = new ThirdParty("test", "testhash");
        LoginDetails loginDetails1 = new LoginDetails(2L, "testname1", "testpassword1", thirdParty);
        CustomUserDetails customUserDetails1 = new CustomUserDetails(loginDetails1);
        var result = customUserDetails1.getHashedKey();
        assertEquals("testhash", result);
    }

}