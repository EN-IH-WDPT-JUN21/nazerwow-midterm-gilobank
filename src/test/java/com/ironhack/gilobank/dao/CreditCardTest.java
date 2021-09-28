package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class CreditCardTest {


    @Test
    void testRemainingBalance(){
        Address testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");

        AccountHolder testHolder1 = new AccountHolder("Test1", "TestSur1", LocalDate.now(), testAddress1, null);


        CreditCard testAccount1 = new CreditCard(
                "secretKey1",
                testHolder1,                    // Primary Holder
                null,                    // Secondary Holder
                new Money(new BigDecimal("-500.00")),      // balance
                new Money(new BigDecimal("10")),       // penaltyFee
                LocalDate.now(),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("-6000.00")),     // Credit Limit Balance
                new BigDecimal("0.1"));     // interestRate

        assertEquals(new BigDecimal("5500.00"), testAccount1.remainingBalance().getAmount());
    }
}
