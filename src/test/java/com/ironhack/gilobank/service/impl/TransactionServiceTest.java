package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class TransactionServiceTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;
    private CheckingAccount testAccount1;
    private CheckingAccount testAccount2;
    private CheckingAccount testAccount3;

    @BeforeEach
    void setUp() throws ParseException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
        LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");

        loginDetails1 = new LoginDetails("hackerman", "ihackthings");
        loginDetails2 = new LoginDetails("testusername2", "testpass2");

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder(loginDetails1, "Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder(loginDetails2, "Test2", "TestSur2", testDateOfBirth2, testAddress2, null);

        testAccount1 = new CheckingAccount(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new BigDecimal("1000"),     // balance
                new BigDecimal("10"),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("11"),      // monthly maintenance Balance
                new BigDecimal("100"));      // minimum balance
        testAccount2 = new CheckingAccount(
                "secretKey2",
                testHolder1,                    // Primary Holder
                null,
                new BigDecimal("2000"),     // balance
                new BigDecimal("20"),       // penaltyFee
                LocalDate.parse("2012-02-02"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("22"),      // monthly maintenance
                new BigDecimal("200"));      // minimum balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new BigDecimal("3000"),     // balance
                new BigDecimal("30"),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new BigDecimal("300"),      // monthly maintenance
                new BigDecimal("3"));      // minimum balance

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void createTransactionLogCredit_TestValidCreation() {
        var sizeBefore = transactionRepository.findAll().size();
        transactionService.createTransactionLogCredit(testAccount1, new BigDecimal("250.00"));
        var sizeAfter = transactionRepository.findAll().size();
        assertEquals(sizeAfter, sizeBefore + 1);
    }

    @Test
    void createTransactionLogCredit_TestDateAutoGenerated() {
        Transaction testTransaction = transactionService.createTransactionLogCredit(testAccount1, new BigDecimal("250.00"));
        assertTrue(testTransaction.getTimeOfTrns().toString().contains(LocalDate.now().toString()));
    }

    @Test
    void createTransactionLogCredit(){
        Transaction testTransaction = transactionService.createTransactionLogCredit(testAccount1, new BigDecimal("250.00"), LocalDateTime.parse("2012-01-01T10:15:30"));
        assertEquals(LocalDateTime.parse("2012-01-01T10:15:30"), testTransaction.getTimeOfTrns());
    };

    // Debits

    @Test
    void createTransactionLogDebit_TestValidCreation() {
        var sizeBefore = transactionRepository.findAll().size();
        Transaction testTransaction = transactionService.createTransactionLogDebit(testAccount1, new BigDecimal("250.00"));
        var sizeAfter = transactionRepository.findAll().size();
        assertEquals(sizeAfter, sizeBefore + 1);
    }

    @Test
    void createTransactionLogDebit_TestDateAutoGenerated() {
        Transaction testTransaction = transactionService.createTransactionLogCredit(testAccount1, new BigDecimal("250.00"));
        assertTrue(testTransaction.getTimeOfTrns().toString().contains(LocalDate.now().toString()));
    }

    @Test
    void createTransactionLogDebit(){
        Transaction testTransaction = transactionService.createTransactionLogDebit(testAccount1, new BigDecimal("250.00"), LocalDateTime.parse("2012-01-01T10:15:30"));
        assertEquals(LocalDateTime.parse("2012-01-01T10:15:30"), testTransaction.getTimeOfTrns());
    };

    // Transfer
    @Test
    void createTransactionLogTransfer(){
        Transaction debitTransaction = transactionService.createTransactionLogTransfer(testAccount1, new BigDecimal("250.00"), testAccount2, LocalDateTime.parse("2012-01-01T10:15:30")).get(0);
        Transaction creditTransaction = transactionService.createTransactionLogTransfer(testAccount1, new BigDecimal("250.00"), testAccount2, LocalDateTime.parse("2012-01-01T10:15:30")).get(1);
        assertEquals(TransactionType.TRANSFER_DEBIT, debitTransaction.getType());
        assertEquals(TransactionType.TRANSFER_CREDIT, creditTransaction.getType());
    };

    @Test
    void findByDateTimeBetween() {
        Transaction testTransaction1 = new Transaction(testAccount1, "Test1", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction2 = new Transaction(testAccount1, "Test2", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));
        Transaction testTransaction3 = new Transaction(testAccount1, "Test2", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-03-03T10:15:30"));
        Transaction testTransaction4 = new Transaction(testAccount1, "Test3", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-04-03T10:15:30"));
        Transaction testTransaction5 = new Transaction(testAccount1, "Test4", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-05-03T10:15:30"));
        Transaction testTransaction6 = new Transaction(testAccount1, "Test5", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-06-03T10:15:30"));
        Transaction testTransaction7 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-07-03T10:15:30"));
        Transaction testTransaction8 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-08-03T10:15:30"));
        Transaction testTransaction9 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-09-03T10:15:30"));

        Transaction testTransaction10 = new Transaction(testAccount2, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction11 = new Transaction(testAccount2, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));

        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction7, testTransaction8, testTransaction9));

        var transactionList = transactionService.findByDateTimeBetween(testAccount1, LocalDateTime.parse("2020-01-03T10:15:20"), LocalDateTime.parse("2020-04-03T10:30:30"));
        assertEquals(4, transactionList.size());
    }

    @Test
    void creditFunds() {
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(), new BigDecimal("250"));
        transactionService.creditFunds(transactionDTO);
        var updatedAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber());
        assertEquals(testAccount1.getBalance().add(new BigDecimal("250.00")), updatedAccount.get().getBalance());
    }

    @Test
    void debitFunds() {
        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("250"), testAccount1.getAccountNumber());
        transactionService.debitFunds(transactionDTO);
        var updatedAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        assertEquals(testAccount1.getBalance().subtract(new BigDecimal("250.00")), updatedAccount.get().getBalance());
    }

    @Test
    void transferBetweenAccounts() {
        TransactionDTO transactionDTO = new TransactionDTO(testAccount1.getAccountNumber(), new BigDecimal("250"), testAccount2.getAccountNumber());
        transactionService.transferBetweenAccounts(transactionDTO);
        var updatedDebitedAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        var updatedCreditAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber());
        assertEquals(testAccount2.getBalance().subtract(new BigDecimal("250.00")), updatedDebitedAccount.get().getBalance());
        assertEquals(testAccount1.getBalance().add(new BigDecimal("250.00")), updatedCreditAccount.get().getBalance());
    }

    @Test
    void findTransactionBetween() {
        // should contain
        Transaction testTransaction1 = new Transaction(testAccount1, "Test1", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));
        Transaction testTransaction2 = new Transaction(testAccount1, "Test2", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-02-03T10:15:30"));
        Transaction testTransaction3 = new Transaction(testAccount1, "Test2", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-03-03T10:15:30"));
        Transaction testTransaction4 = new Transaction(testAccount1, "Test3", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-04-03T10:15:30"));
        Transaction testTransaction5 = new Transaction(testAccount1, "Test4", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-05-03T10:15:30"));
        // Will not contain
        Transaction testTransaction6 = new Transaction(testAccount1, "Test5", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-06-03T10:15:30"));
        Transaction testTransaction10 = new Transaction(testAccount2, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), LocalDateTime.parse("2020-01-03T10:15:30"));

        transactionRepository.saveAll(List.of(testTransaction1, testTransaction2, testTransaction3, testTransaction4,
                testTransaction5, testTransaction6, testTransaction10));

        var listOfTransactions = transactionService.findTransactionBetween(testAccount1.getAccountNumber(),
                LocalDate.parse("2020-01-03"), LocalDate.parse("2020-05-03"));

        assertEquals(5, listOfTransactions.size());
        assertFalse(listOfTransactions.contains(testTransaction10));
        assertFalse(listOfTransactions.contains(testTransaction6));
    }

    @Test
    void checkForFraud_Test_No_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("250"), testAccount1.getAccountNumber());

        assertDoesNotThrow(() -> transactionService.checkForFraud(transactionDTO));
    }

    @Test
    void checkForFraud_Test_1Second_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("10.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("250"), testAccount1.getAccountNumber());

        assertThrows(ResponseStatusException.class, () -> transactionService.checkForFraud(transactionDTO));
    }

    @Test
    void checkForFraud_Test_24Hour_Fraud_Detected() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("2500000"), testAccount1.getAccountNumber());
        assertThrows(ResponseStatusException.class, () -> transactionService.checkForFraud(transactionDTO));
    }

    @Test
    void checkForFraud_Test_Account_Frozen() {
        Transaction test1 = new Transaction(testAccount1, "Test6", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now());
        Transaction test2 = new Transaction(testAccount1, "Test7", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(3));
        Transaction test3 = new Transaction(testAccount1, "Test8", new BigDecimal("250.00"), testAccount1.getBalance(), TransactionType.DEBIT, LocalDateTime.now().plusSeconds(5));
        transactionRepository.saveAll(List.of(test1, test2, test3));

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal("2500000"), testAccount1.getAccountNumber());
        Optional<CheckingAccount> optionalCheckingAccount =
                checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        assertEquals(Status.ACTIVE, optionalCheckingAccount.get().getStatus());
        try {
            transactionService.checkForFraud(transactionDTO);
        } catch (ResponseStatusException e) {
            optionalCheckingAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
            assertEquals(Status.FROZEN, optionalCheckingAccount.get().getStatus());
        }
    }


    @Test
    void checkAccountStatus_Active() {
        assertDoesNotThrow(() -> transactionService.checkAccountStatus(testAccount1));
    }

    @Test
    void checkAccountStatus_Frozen() {
        testAccount1.setStatus(Status.FROZEN);
        checkingAccountRepository.save(testAccount1);
        assertThrows(ResponseStatusException.class, () -> transactionService.checkAccountStatus(testAccount1));
    }

    @Test
    void checkAvailableFunds_ThrowsException() {
        assertThrows(ResponseStatusException.class, () -> transactionService.checkAvailableFunds(testAccount1, testAccount1.getBalance().add(testAccount1.getBalance())));
    }

    @Test
    void checkAvailableFunds_DoesNotThrow(){
        assertDoesNotThrow(() -> transactionService.checkAvailableFunds(testAccount1, testAccount1.getBalance()));
    }

    @Test
    void penaltyCheck_Deducts_Penalty(){
        testAccount1.setBalance(new BigDecimal("99.00"));
        var balanceAfterFee = transactionService.penaltyCheck(testAccount1, testAccount1.getMinimumBalance(), testAccount1.getPenaltyFee());
        assertEquals(new BigDecimal("99.00").subtract(testAccount1.getPenaltyFee()), balanceAfterFee);
    }

    @Test
    void penaltyCheck_Does_Not_Deduct(){
        testAccount1.setBalance(new BigDecimal("100.00"));
        var balanceAfterFee = transactionService.penaltyCheck(testAccount1, testAccount1.getMinimumBalance(), testAccount1.getPenaltyFee());
        assertEquals(new BigDecimal("100.00"), balanceAfterFee);
    }
}