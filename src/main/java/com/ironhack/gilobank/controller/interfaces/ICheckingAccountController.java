package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.BalanceDTO;
import com.ironhack.gilobank.controller.dto.CheckingAccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ICheckingAccountController {

    CheckingAccount getByAccountNumber(Long accountNumber);

    List<CheckingAccount> getAll();

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferFunds(TransactionDTO transactionDTO);

    List<Transaction> getTransactionsByDateBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    Account createCheckingAccount(CheckingAccountDTO checkingAccountDTO);

    Account updateCheckingAccount(Long accountNumber, CheckingAccountDTO checkingAccountDTO);

    BalanceDTO getBalance(Long accountNumber);
}
