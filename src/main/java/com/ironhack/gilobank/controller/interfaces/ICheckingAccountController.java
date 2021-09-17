package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;

import java.util.List;

public interface ICheckingAccountController {

    CheckingAccount getByAccountNumber(Long accountNumber);

    List<CheckingAccount> getAll();

    void creditFunds(TransactionDTO transactionDTO);

    void debitFunds(TransactionDTO transactionDTO);

    void transferFunds(TransactionDTO transactionDTO);
}
