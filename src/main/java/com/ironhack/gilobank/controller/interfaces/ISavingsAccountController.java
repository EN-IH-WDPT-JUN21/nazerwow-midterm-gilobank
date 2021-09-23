package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.SavingsAccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.SavingsAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ISavingsAccountController {

    List<SavingsAccount> getAll();

    SavingsAccount getByAccountNumber(Long accountNumber);

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferFunds(TransactionDTO transactionDTO);

    List<Transaction> getTransactionsByDateBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    SavingsAccount createSavingsAccount(SavingsAccountDTO savingsAccountDTO);

    SavingsAccount updateSavingsAccount(Long accountNumber, SavingsAccountDTO savingsAccountDTO);
}
