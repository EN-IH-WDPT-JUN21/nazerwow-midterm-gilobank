package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ITransactionService {

    Transaction createTransactionLog(Account account, BigDecimal amount);
    Transaction createTransactionLog(Account account, BigDecimal amount, LocalDate date);
}
