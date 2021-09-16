package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.controller.interfaces.ICheckingAccountController;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account/checking")
public class CheckingAccountController implements ICheckingAccountController {

    @Autowired
    private ICheckingAccountService checkingAccountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CheckingAccount> getByAccountNumber(@PathVariable(name = "id") Long accountNumber) {
        return checkingAccountService.findByAccountNumber(accountNumber);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> getAll() {
        return checkingAccountService.findAll();
    }

    @PutMapping("/credit")
    @ResponseStatus(HttpStatus.OK)
    public void creditFunds(@RequestBody TransactionDTO transactionDTO) {
        checkingAccountService.creditFunds(transactionDTO);
    }

    @PutMapping("/debit")
    @ResponseStatus(HttpStatus.OK)
    public void debitFunds(@RequestBody TransactionDTO transactionDTO) {
        checkingAccountService.debitFunds(transactionDTO);
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferFunds(@RequestBody TransactionDTO transactionDTO) {
        checkingAccountService.transferBetweenAccounts(transactionDTO);
    }

    @GetMapping("/{id}/{dateFrom}/{dateTo}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactionsByDateBetween(
            @PathVariable(name="id") Long accountNumber,
            @PathVariable(name="dateFrom")
            @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")LocalDateTime startPoint,
            @PathVariable(name="dateTo")
            @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")LocalDateTime endPoint)
    {
        return checkingAccountService.findTransactionBetween(accountNumber, startPoint, endPoint);
    }

}
