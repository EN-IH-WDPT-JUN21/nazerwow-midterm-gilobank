package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.AccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ICheckingAccountService {

    Optional<CheckingAccount> findByAccountNumber(Long accountNumber);
    List<CheckingAccount> findAll();
    void creditFunds(TransactionDTO transactionDTO);
    void debitFunds(TransactionDTO transactionDTO);
    void transferBetweenAccounts(TransactionDTO transactionDTO);
}
