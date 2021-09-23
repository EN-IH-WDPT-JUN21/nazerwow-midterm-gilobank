package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.SavingsAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ISavingsAccountService {

    List<SavingsAccount> findAll();

    SavingsAccount findByAccountNumber(Long accountNumber);
    Optional<SavingsAccount> findByAccountNumberOptional(Long accountNumber);

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferBetweenAccounts(TransactionDTO transactionDTO);

    List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    void saveNewSavingsAccount(SavingsAccount savingsAccount);
}
