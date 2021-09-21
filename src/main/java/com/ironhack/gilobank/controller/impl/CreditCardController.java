package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.controller.interfaces.ICreditCardController;
import com.ironhack.gilobank.dao.CreditCard;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/account/creditcard")
public class CreditCardController implements ICreditCardController {

    @Autowired
    private ICreditCardService creditCardService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getByAccountNumber(@PathVariable(name = "id") Long accountNumber) {
        return creditCardService.findByAccountNumber(accountNumber);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> getAll() {
        return creditCardService.findAll();
    }

    @PutMapping("/credit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction creditFunds(@RequestBody TransactionDTO transactionDTO) {
        return creditCardService.creditFunds(transactionDTO);
    }

    @PutMapping("/debit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction debitFunds(@RequestBody TransactionDTO transactionDTO) {
        return creditCardService.debitFunds(transactionDTO);
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Transaction transferFunds(@RequestBody TransactionDTO transactionDTO) {
        return creditCardService.transferBetweenAccounts(transactionDTO);
    }

    // Allows you to pass dates without time for enhanced customer experience
    @GetMapping("/{id}/{dateFrom}/{dateTo}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactionsByDateBetween(
            @PathVariable(name = "id") Long accountNumber,
            @PathVariable(name = "dateFrom")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startPoint,
            @PathVariable(name = "dateTo")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endPoint) {
        return creditCardService.findTransactionBetween(accountNumber, startPoint, endPoint);
    }
}
