package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.AccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ICheckingAccountService {

    Optional<CheckingAccount> findByAccountNumber(Long accountNumber);
    List<CheckingAccount> findAll();
    void creditFunds(TransactionDTO transactionDTO);
    void debitFunds(TransactionDTO transactionDTO);
    void transferBetweenAccounts(TransactionDTO transactionDTO);
    List<Transaction> findTransactionBetween(Long accountNumber, LocalDateTime startDate, LocalDateTime endDate);
    void checkForFraud(TransactionDTO transactionDTO);
}
