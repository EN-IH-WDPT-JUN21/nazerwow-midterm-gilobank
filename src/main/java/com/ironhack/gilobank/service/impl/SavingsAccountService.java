package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.SavingsAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.SavingsAccountRepository;
import com.ironhack.gilobank.service.interfaces.ISavingsAccountService;
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
public class SavingsAccountService implements ISavingsAccountService {

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private FraudDetection fraudDetection;


    public List<SavingsAccount> findAll() {
        return savingsAccountRepository.findAllSecure();
    }

    public SavingsAccount findByAccountNumber(Long accountNumber) {
        Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findByAccountNumberSecure(accountNumber);
        if (savingsAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + accountNumber);
        transactionService.applyInterestYearly(savingsAccount.get().getAccountNumber(),
                savingsAccount.get().getBalance(),
                savingsAccount.get().getInterestRate());
        return savingsAccount.get();
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
        SavingsAccount savingsAccount = findByAccountNumber(accountNumber);
        LocalDateTime convertedStartDate = startDate.atStartOfDay();
        LocalDateTime convertedEndDate = endDate.atTime(23, 59, 59);
        return transactionService.findByDateTimeBetween(savingsAccount, convertedStartDate, convertedEndDate);
    }


}
