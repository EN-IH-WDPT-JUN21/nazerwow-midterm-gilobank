package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.TransactionRepository;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import com.ironhack.gilobank.utils.Money;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void createTransactionLog(Account account, BigDecimal amount, Optional<LocalDate> date) {
        LocalDate transactionDate;
        Money moneyAmount = new Money(amount);
        String transactionName;
        if(date.isPresent()) {
            transactionDate = date.get();
        } else { transactionDate = LocalDate.now();
        }
        if(amount.longValue() >= 0) {
            transactionName = moneyAmount + " credited on: " + transactionDate;
        }
        else {
            transactionName = moneyAmount + " debited on: " + transactionDate;
        }
        transactionRepository.save(new Transaction(account, transactionName, amount, transactionDate));
    }


    public Transaction createTransactionLog(Account account, BigDecimal amount) {
        LocalDate transactionDate = LocalDate.now();
        String transactionName;
        Money moneyAmount = new Money(amount);
        if(amount.longValue() >= 0) {
            transactionName = moneyAmount + " credited on: " + transactionDate;
        }
        else {
            transactionName = moneyAmount + " debited on: " + transactionDate;
        }
        Transaction transaction = new Transaction(account, transactionName, amount, transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLog(Account account, BigDecimal amount, LocalDate date) {
        Money moneyAmount = new Money(amount);
        String transactionName;
        if(amount.longValue() >= 0) {
            transactionName = moneyAmount + " credited on: " + date;
        }
        else {
            transactionName = moneyAmount + " debited on: " + date;
        }
        Transaction transaction = new Transaction(account, transactionName, amount, date);
        transactionRepository.save(transaction);
        return transaction;
    }
}
