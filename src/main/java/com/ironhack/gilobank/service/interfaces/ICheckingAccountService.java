package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICheckingAccountService {

    Optional<CheckingAccount> findByAccountNumberOptional(Long accountNumber);

    CheckingAccount findByAccountNumber(Long accountNumber);

    List<CheckingAccount> findAll();

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferBetweenAccounts(TransactionDTO transactionDTO);

    List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    void saveCheckingAccount(CheckingAccount checkingAccount);

}
