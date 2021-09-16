package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.CheckingAccountRepository;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
import com.ironhack.gilobank.utils.FraudDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    public Optional<CheckingAccount> findByAccountNumber(Long accountNumber) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(accountNumber);
        if(!checkingAccount.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + accountNumber);
        return checkingAccount;
    }

    public List<CheckingAccount> findAll() {
        return checkingAccountRepository.findAll();
    }

    public void creditFunds(TransactionDTO transactionDTO) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber());
        if(!checkingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + transactionDTO.getCreditAccountNumber());
        }else{
            checkingAccount.get().credit(transactionDTO.getAmount());
            checkingAccountRepository.save(checkingAccount.get());
            transactionService.createTransactionLogCredit(checkingAccount.get(), transactionDTO.getAmount());
        }
    }

    public void debitFunds(TransactionDTO transactionDTO) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        if(!checkingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + transactionDTO.getDebitAccountNumber());
        }else{
            checkingAccount.get().debit(transactionDTO.getAmount());
            checkingAccountRepository.save(checkingAccount.get());
            transactionService.createTransactionLogDebit(checkingAccount.get(), transactionDTO.getAmount());
        }
    }

    public void transferBetweenAccounts(TransactionDTO transactionDTO){
        Optional<CheckingAccount> debitAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        Optional<CheckingAccount> creditAccount = checkingAccountRepository.findById(transactionDTO.getCreditAccountNumber());
        if(!debitAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + debitAccount);
        }else if(!creditAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + creditAccount);
        }else{
            debitAccount.get().debit(transactionDTO.getAmount());
            creditAccount.get().credit(transactionDTO.getAmount());
            checkingAccountRepository.saveAll(List.of(debitAccount.get(), creditAccount.get()));
            transactionService.createTransactionLogTransfer(debitAccount.get(), transactionDTO.getAmount(), creditAccount.get());
        }
    }

    public List<Transaction> findTransactionBetween(Long accountNumber, LocalDateTime startDate, LocalDateTime endDate){
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(accountNumber);
        if(!checkingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + accountNumber);
        }
        return transactionService.findByDateTimeBetween(checkingAccount.get(), startDate, endDate);
    }

    public void checkForFraud(TransactionDTO transactionDTO){
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(transactionDTO.getDebitAccountNumber());
        if(fraudDetection.fraudDetector(checkingAccount.get(), transactionDTO.getAmount())){
            checkingAccount.get().freezeAccount();
            checkingAccountRepository.save(checkingAccount.get());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction Denied: Please contact us for details:");
        }
    }

}
