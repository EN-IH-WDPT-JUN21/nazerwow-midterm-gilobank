package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ICheckingAccountService {

    CheckingAccount findByAccountNumber(Long accountNumber);

    List<CheckingAccount> findAll();

    void creditFunds(TransactionDTO transactionDTO);

    void debitFunds(TransactionDTO transactionDTO);

    void transferBetweenAccounts(TransactionDTO transactionDTO);

    List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    void checkForFraud(TransactionDTO transactionDTO);

    void checkAccountStatus(CheckingAccount checkingAccount);
}
