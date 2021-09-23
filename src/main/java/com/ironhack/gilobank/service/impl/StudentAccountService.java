package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.StudentAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.StudentAccountRepository;
import com.ironhack.gilobank.service.interfaces.IStudentAccountService;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import com.ironhack.gilobank.utils.FraudDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentAccountService implements IStudentAccountService {

    @Autowired
    private StudentAccountRepository studentAccountRepository;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private FraudDetection fraudDetection;

    public List<StudentAccount> findAll() {
        return studentAccountRepository.findAll();
    }

    @Override
    public StudentAccount findByAccountNumber(Long accountNumber) {
        Optional<StudentAccount> optionalStudentAccount = studentAccountRepository.findById(accountNumber);
        if (optionalStudentAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Student Account Found with Account Number :" + accountNumber);
        if (!transactionService.checkAuthentication(accountNumber))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access this page");
        return optionalStudentAccount.get();

    }

    public Optional<StudentAccount> findByAccountNumberOptional(Long accountNumber) {
        Optional<StudentAccount> studentAccount = studentAccountRepository.findById(accountNumber);
        return studentAccount;
    }

    public Transaction creditFunds(TransactionDTO transactionDTO) {
        findByAccountNumber(transactionDTO.getCreditAccountNumber());
        return transactionService.creditFunds(transactionDTO);
    }


    public Transaction debitFunds(TransactionDTO transactionDTO) {
        findByAccountNumber(transactionDTO.getDebitAccountNumber());
        return transactionService.debitFunds(transactionDTO);
    }

    public Transaction transferBetweenAccounts(TransactionDTO transactionDTO) {
        findByAccountNumber(transactionDTO.getDebitAccountNumber());
        return transactionService.transferBetweenAccounts(transactionDTO);
    }

    // Converts LocalDate to LocalDateTime - Gets transaction from start of startDate to end of endDate
    public List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate) {
        StudentAccount studentAccount = findByAccountNumber(accountNumber);
        LocalDateTime convertedStartDate = startDate.atStartOfDay();
        LocalDateTime convertedEndDate = endDate.atTime(23, 59, 59);
        return transactionService.findByDateTimeBetween(studentAccount, convertedStartDate, convertedEndDate);
    }

    public void saveNewStudentAccount(StudentAccount studentAccount) {
        studentAccountRepository.save(studentAccount);
    }

}
