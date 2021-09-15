package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITransactionService {

    Transaction createTransactionLogCredit(Account account, BigDecimal amount);
    Transaction createTransactionLogCredit(Account account, BigDecimal amount, LocalDate date);
    Transaction createTransactionLogDebit(Account account, BigDecimal amount);
    Transaction createTransactionLogDebit(Account account, BigDecimal amount, LocalDate date);
    List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount);
    List<Transaction> createTransactionLogTransfer(Account debitAccount, BigDecimal amount, Account creditAccount, LocalDate date);
}
