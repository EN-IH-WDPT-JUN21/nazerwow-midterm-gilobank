package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.BalanceDTO;
import com.ironhack.gilobank.controller.dto.CreditCardDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.CreditCard;
import com.ironhack.gilobank.dao.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ICreditCardController {

    CreditCard getByAccountNumber(Long accountNumber);

    List<CreditCard> getAll();

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferFunds(TransactionDTO transactionDTO);

    List<Transaction> getTransactionsByDateBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    CreditCard createCreditCard(CreditCardDTO creditCardDTO);

    CreditCard updateCreditCard(Long accountNumber, CreditCardDTO creditCardDTO);
    BalanceDTO getBalance(Long  accountNumber);
}
