package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class TransactionRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private Address testAddress1, testAddress2;
    private AccountHolder testHolder1, testHolder2;
    private LoginDetails loginDetails1, loginDetails2;
    private SavingsAccount testAccount1;
    private Transaction testTransaction1, testTransaction2, testTransaction3, testTransaction4, testTransaction5, testTransaction6, testTransaction7, testTransaction8, testTransaction9;


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

        testAccount1 = new SavingsAccount(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new Money(     new BigDecimal("1000")),     // balance
                        new Money(     new BigDecimal("10")),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                                new Money(    new BigDecimal("100")),      // Minimum Balance
                                        new BigDecimal("0.1"));      // interestRate


        testTransaction1 = new Transaction(testAccount1, "Test1",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-01-03T10:12:30"));
        testTransaction2 = new Transaction(testAccount1, "Test2",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.parse("2020-02-03T10:15:30"));
        testTransaction3 = new Transaction(testAccount1, "Test2",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.TRANSFER_DEBIT, LocalDateTime.parse("2020-03-03T10:11:30"));
        testTransaction4 = new Transaction(testAccount1, "Test3",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.TRANSFER_DEBIT, LocalDateTime.parse("2020-04-03T10:12:30"));
        testTransaction5 = new Transaction(testAccount1, "Test4",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.CREDIT, LocalDateTime.parse("2020-05-03T10:10:30"));
        testTransaction6 = new Transaction(testAccount1, "Test5",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.CREDIT, LocalDateTime.parse("2020-06-03T10:18:30"));
        testTransaction7 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.TRANSFER_CREDIT, LocalDateTime.parse("2020-07-03T10:10:30"));
        testTransaction8 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.TRANSFER_CREDIT, LocalDateTime.parse("2020-08-03T10:15:30"));
        testTransaction9 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.CREDIT, LocalDateTime.parse("2020-09-03T10:22:30"));

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        savingsAccountRepository.save(testAccount1);
        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction7, testTransaction8, testTransaction9));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findByTimeOfTrnsBetween_Valid() {
        var transactionList = transactionRepository.findByTimeOfTrnsBetween(testAccount1, LocalDateTime.parse("2020-01-03T10:15:20"), LocalDateTime.parse("2020-04-03T10:30:30"));
        assertEquals(3, transactionList.size());
    }

    @Test
    void findByTimeOfTrnsBetween_Find_None() {
        var transactionList = transactionRepository.findByTimeOfTrnsBetween(testAccount1, LocalDateTime.parse("1980-01-03T10:15:20"), LocalDateTime.parse("2000-04-03T10:30:30"));
        assertEquals(0, transactionList.size());
    }

    @Test
    void debitsFromLast24Hour() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money(  new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.CREDIT, LocalDateTime.now());
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var transactionList = transactionRepository.allDebitsFromLast24Hour(testAccount1);
        assertEquals(3, transactionList.size());
    }

    @Test
    void totalDebitsOfLast24Hours() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.CREDIT, LocalDateTime.now());
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var total = transactionRepository.totalOfAllDebitsFromLast24Hours(testAccount1);
        assertEquals(new BigDecimal("750.00"), total);
    }

    @Test
    void historicDailyTotals_Test() {
        var total = transactionRepository.historicDailyTotals(testAccount1);
        assertEquals(9, total.size());
    }

    @Test
    void debitsWithin1Second() {
        Transaction test1 = new Transaction(testAccount1, "Test6",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8",new Money( new BigDecimal("250.00")), testAccount1.getBalance(), TransactionType.CREDIT, LocalDateTime.now());
        transactionRepository.saveAll(List.of(test1, test2, test3));
        var total = transactionRepository.debitsWithin1Second(testAccount1);
        assertEquals(test1.getTimeOfTrns().getSecond(), total.get(0).toLocalDateTime().getSecond());
    }


}