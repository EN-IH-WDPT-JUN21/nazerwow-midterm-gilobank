package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.CheckingAccountRepository;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
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
public class CheckingAccountService implements ICheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private FraudDetection fraudDetection;

    public CheckingAccount findByAccountNumber(Long accountNumber) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(accountNumber);
        if (checkingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + accountNumber);
        return checkingAccount.get();
    }

    public List<CheckingAccount> findAll() {
        return checkingAccountRepository.findAll();
    }


    public Transaction creditFunds(TransactionDTO transactionDTO) {
        return transactionService.creditFunds(transactionDTO);
    }


    public Transaction debitFunds(TransactionDTO transactionDTO) {
        return transactionService.debitFunds(transactionDTO);
    }


    public Transaction transferBetweenAccounts(TransactionDTO transactionDTO) {
        return transactionService.transferBetweenAccounts(transactionDTO);
    }

    // Converts LocalDate to LocalDateTime - Gets transaction from start of startDate to end of endDate
    public List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate) {
        CheckingAccount checkingAccount = findByAccountNumber(accountNumber);
        LocalDateTime convertedStartDate = startDate.atStartOfDay();
        LocalDateTime convertedEndDate = endDate.atTime(23, 59, 59);
        return transactionService.findByDateTimeBetween(checkingAccount, convertedStartDate, convertedEndDate);
    }


}
