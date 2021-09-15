package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.AccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ICheckingAccountController {

    Optional<CheckingAccount> getByAccountNumber(AccountDTO accountDTO);
    List<CheckingAccount> getAll();

    void creditFunds(TransactionDTO transactionDTO);
    void debitFunds(TransactionDTO transactionDTO);
    void transferFunds(TransactionDTO transactionDTO);
}
