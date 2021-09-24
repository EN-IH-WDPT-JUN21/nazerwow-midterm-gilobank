package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.BalanceDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.StudentAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStudentAccountService {

    List<StudentAccount> findAll();

    StudentAccount findByAccountNumber(Long accountNumber);

    Optional<StudentAccount> findByAccountNumberOptional(Long accountNumber);

    void saveNewStudentAccount(StudentAccount studentAccount);

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferBetweenAccounts(TransactionDTO transactionDTO);

    List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    BalanceDTO getBalance(Long accountNumber);
}
