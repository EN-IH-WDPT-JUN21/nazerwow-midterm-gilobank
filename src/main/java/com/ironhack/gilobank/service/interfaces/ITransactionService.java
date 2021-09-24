package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.BalanceDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionService {

    Transaction createTransactionLog(Account account, TransactionDTO transactionDTO);

    List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount);

    List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount, LocalDateTime date);

    List<Transaction> findByDateTimeBetween(Account account, LocalDateTime startPoint, LocalDateTime endPoint);

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferBetweenAccounts(TransactionDTO transactionDTO);

    List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    void checkForFraud(TransactionDTO transactionDTO);

    void checkAccountStatus(Account checkingAccount);

    void findAccountTypeAndSave(Account account);

    Account findAccountTypeAndReturn(Long accountNumber);

    void checkAvailableFunds(Account account, BigDecimal amount);

    BigDecimal penaltyCheck(Account account, BigDecimal minimumBalance, BigDecimal penaltyFee);

    boolean interestMonthlyCheck(Long accountNumber);

    boolean interestYearlyCheck(Long accountNumber);

    void applyInterestYearly(Long accountNumber, BigDecimal balance, BigDecimal interestRate);

    void applyInterestMonthly(Long accountNumber, BigDecimal balance, BigDecimal interestRate);

    boolean checkAuthentication(Long accountNumber);

    boolean verifyThirdParty(String hashKey);

    boolean verifySecretKey(String secretKey, Account account);

    BalanceDTO getBalance(Account account);
}
