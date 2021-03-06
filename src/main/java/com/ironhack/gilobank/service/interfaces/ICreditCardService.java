package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.BalanceDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CreditCard;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.utils.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ICreditCardService {

    List<CreditCard> findAll();

    CreditCard findByAccountNumber(Long accountNumber);

    Optional<CreditCard> findByAccountNumberOptional(Long accountNumber);

    Transaction creditFunds(TransactionDTO transactionDTO);

    Transaction debitFunds(TransactionDTO transactionDTO);

    Transaction transferBetweenAccounts(TransactionDTO transactionDTO);

    List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate);

    void saveNewCreditCard(CreditCard creditCard);

    BalanceDTO getBalance(Long accountNumber);

    Money getRemainingBalance(Long accountNumber);

    boolean availableFunds(TransactionDTO transactionDTO);
}
