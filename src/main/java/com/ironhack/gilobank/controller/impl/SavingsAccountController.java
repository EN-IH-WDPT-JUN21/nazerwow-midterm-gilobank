package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.SavingsAccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.controller.interfaces.ISavingsAccountController;
import com.ironhack.gilobank.dao.SavingsAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.service.interfaces.ICreationService;
import com.ironhack.gilobank.service.interfaces.ISavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/account/saving")
public class SavingsAccountController implements ISavingsAccountController {

    @Autowired
    private ISavingsAccountService savingsAccountService;
    @Autowired
    private ICreationService creationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getAll() {
        return savingsAccountService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SavingsAccount getByAccountNumber(@PathVariable(name = "id") Long accountNumber) {
        return savingsAccountService.findByAccountNumber(accountNumber);
    }

    @PutMapping("/credit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction creditFunds(@RequestBody TransactionDTO transactionDTO) {
        return savingsAccountService.creditFunds(transactionDTO);
    }

    @PutMapping("/debit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction debitFunds(@RequestBody TransactionDTO transactionDTO) {
        return savingsAccountService.debitFunds(transactionDTO);
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Transaction transferFunds(@RequestBody TransactionDTO transactionDTO) {
        return savingsAccountService.transferBetweenAccounts(transactionDTO);
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
        return savingsAccountService.findTransactionBetween(accountNumber, startPoint, endPoint);
    }

    @PutMapping("/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SavingsAccount createSavingsAccount(@RequestBody SavingsAccountDTO savingsAccountDTO) {
        return creationService.newSavingsAccount(savingsAccountDTO);
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SavingsAccount updateSavingsAccount(@PathVariable(name = "id") Long accountNumber,
                                               @RequestBody SavingsAccountDTO savingsAccountDTO) {
        savingsAccountService.findByAccountNumber(accountNumber);
        savingsAccountDTO.setAccountNumber(accountNumber);
        return creationService.newSavingsAccount(savingsAccountDTO);
    }
}
