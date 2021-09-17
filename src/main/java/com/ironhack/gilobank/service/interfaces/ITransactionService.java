package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionService {

    Transaction createTransactionLogCredit(Account account, BigDecimal amount);

    Transaction createTransactionLogCredit(Account account, BigDecimal amount, LocalDateTime date);

    Transaction createTransactionLogDebit(Account account, BigDecimal amount);

    Transaction createTransactionLogDebit(Account account, BigDecimal amount, LocalDateTime date);

    List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount);

    List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount, LocalDateTime date);

    List<Transaction> findByDateTimeBetween(Account account, LocalDateTime startPoint, LocalDateTime endPoint);
}
