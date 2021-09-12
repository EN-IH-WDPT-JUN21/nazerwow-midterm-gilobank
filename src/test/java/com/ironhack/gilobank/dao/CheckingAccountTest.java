package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import org.hibernate.annotations.Check;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CheckingAccountTest {

//    private Address testAddress1;
//    private Address testAddress2;
//    private AccountHolder testHolder1;
//    private AccountHolder testHolder2;
//
//    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
//
//    @BeforeEach
//    void setUp() throws ParseException {
//        Date testDateOfBirth1 = date.parse("1988-01-01");
//        Date testDateOfBirth2 = date.parse("1994-01-01");
//
//        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
//        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");
//
//        testHolder1 = new AccountHolder("testholder1", "testpass1", Role.ACCOUNTHOLDER, "Test1", "TestSur1", testDateOfBirth1,testAddress1);
//        testHolder2 = new AccountHolder("testholder2", "testpass2", Role.ACCOUNTHOLDER, "Test2", "TestSur2", testDateOfBirth2,testAddress2);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void CheckingAccountCreation_TEST_Positive() throws ParseException {
////        CheckingAccount testAccount = new CheckingAccount(testHolder1, new BigDecimal("150"), new BigDecimal("150"), date.parse("2021-09-01"), Status.ACTIVE);
//
//    }
}