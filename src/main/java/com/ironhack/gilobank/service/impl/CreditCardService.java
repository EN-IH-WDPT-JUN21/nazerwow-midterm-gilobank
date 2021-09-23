package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.CreditCard;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.repositories.CreditCardRepository;
import com.ironhack.gilobank.service.interfaces.ICreditCardService;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import com.ironhack.gilobank.utils.FraudDetection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private FraudDetection fraudDetection;


    public List<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    public CreditCard findByAccountNumber(Long accountNumber) {
        Optional<CreditCard> creditCard = creditCardRepository.findById(accountNumber);
        if (creditCard.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Credit card found with Account Number: " + accountNumber);
        transactionService.applyInterestMonthly(creditCard.get().getAccountNumber(),
                creditCard.get().getBalance(),
                creditCard.get().getInterestRate());
        if (!transactionService.checkAuthentication(accountNumber))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to access this page");
        return creditCard.get();
    }

    public Optional<CreditCard> findByAccountNumberOptional(Long accountNumber) {
        Optional<CreditCard> creditCard = creditCardRepository.findById(accountNumber);
        return creditCard;
    }

    public Transaction creditFunds(TransactionDTO transactionDTO) {
        findByAccountNumber(transactionDTO.getCreditAccountNumber());
        return transactionService.creditFunds(transactionDTO);
    }


    public Transaction debitFunds(TransactionDTO transactionDTO) {
        findByAccountNumber(transactionDTO.getDebitAccountNumber());
        return transactionService.debitFunds(transactionDTO);
    }


    public Transaction transferBetweenAccounts(TransactionDTO transactionDTO) {
        findByAccountNumber(transactionDTO.getDebitAccountNumber());
        return transactionService.transferBetweenAccounts(transactionDTO);
    }

    // Converts LocalDate to LocalDateTime - Gets transaction from start of startDate to end of endDate
    public List<Transaction> findTransactionBetween(Long accountNumber, LocalDate startDate, LocalDate endDate) {
        CreditCard creditCard = findByAccountNumber(accountNumber);
        LocalDateTime convertedStartDate = startDate.atStartOfDay();
        LocalDateTime convertedEndDate = endDate.atTime(23, 59, 59);
        return transactionService.findByDateTimeBetween(creditCard, convertedStartDate, convertedEndDate);
    }

    public void saveNewCreditCard(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }


}
