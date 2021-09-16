package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.TransactionRepository;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import com.ironhack.gilobank.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

//    public void createTransactionLog(Account account, BigDecimal amount, Optional<LocalDate> date) {
//        LocalDate transactionDate;
//        Money moneyAmount = new Money(amount);
//        String transactionName;
//        if(date.isPresent()) {
//            transactionDate = date.get();
//        } else { transactionDate = LocalDate.now();
//        }
//        if(amount.longValue() >= 0) {
//            transactionName = moneyAmount + " credited on: " + transactionDate;
//        }
//        else {
//            transactionName = moneyAmount + " debited on: " + transactionDate;
//        }
//        transactionRepository.save(new Transaction(account, transactionName, amount, account.getBalance(), transactionDate));
//    }


    public Transaction createTransactionLogCredit(Account account, BigDecimal amount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        String transactionName;
        Money moneyAmount = new Money(amount);
        transactionName = moneyAmount + " credit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogCredit(Account account, BigDecimal amount, LocalDateTime date) {
        Money moneyAmount = new Money(amount);
        String transactionName;
        transactionName = moneyAmount + " credit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), date);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogDebit(Account account, BigDecimal amount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        String transactionName;
        Money moneyAmount = new Money(amount);
        transactionName = moneyAmount + " debit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), transactionDate);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction createTransactionLogDebit(Account account, BigDecimal amount, LocalDateTime date) {
        Money moneyAmount = new Money(amount);
        String transactionName;
        transactionName = moneyAmount + " debit";
        Transaction transaction = new Transaction(account, transactionName, amount, account.getBalance(), date);
        transactionRepository.save(transaction);
        return transaction;
    }

    public List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount) {
        LocalDateTime transactionDate = LocalDateTime.now();
        Money moneyAmount = new Money(amount);
        String debitName = moneyAmount + " Transfer to Account Number: " + creditAccount.getAccountNumber();
        String creditName = moneyAmount + " Transfer from Account Number: " + debitAccount.getAccountNumber();
        Transaction debit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), transactionDate);
        Transaction credit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), transactionDate);
        transactionRepository.saveAll(List.of(debit, credit));
        return List.of(debit, credit);
    }

    public List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount, LocalDateTime transactionDate) {
        Money moneyAmount = new Money(amount);
        String debitName = moneyAmount + " Transfer to Account Number: " + creditAccount.getAccountNumber();
        String creditName = moneyAmount + " Transfer from Account Number: " + debitAccount.getAccountNumber();
        Transaction debit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), transactionDate);
        Transaction credit = new Transaction(debitAccount, debitName, amount, debitAccount.getBalance(), transactionDate);
        transactionRepository.saveAll(List.of(debit, credit));
        return List.of(debit, credit);
    }

    @Override
    public List<Transaction> findByDateTimeBetween(Account accountNumber, LocalDateTime startPoint, LocalDateTime endPoint) throws ResponseStatusException {
        List<Transaction> transactionList =  transactionRepository.findByTimeOfTrnsBetween(accountNumber, startPoint, endPoint);
        if(transactionList.isEmpty())
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND,  "No transactions found between: " + startPoint + " and " + endPoint);
        return transactionList;
    }

}
