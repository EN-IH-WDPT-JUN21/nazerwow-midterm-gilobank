package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.CheckingAccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.StudentAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface IStudentAccountController {

    List<StudentAccount> getAll();

    StudentAccount getByAccountNumber(Long accountNumber);

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferFunds(TransactionDTO transactionDTO);

    List<Transaction> getTransactionsByDateBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    StudentAccount createStudentAccount(CheckingAccountDTO checkingAccountDTO);

    StudentAccount updateStudentAccount(Long accountNumber, CheckingAccountDTO checkingAccountDTO);
}
