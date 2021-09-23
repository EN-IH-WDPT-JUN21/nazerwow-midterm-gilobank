package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.CheckingAccountDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.controller.interfaces.IStudentAccountController;
import com.ironhack.gilobank.dao.StudentAccount;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.service.interfaces.ICreationService;
import com.ironhack.gilobank.service.interfaces.IStudentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/account/student")
public class StudentAccountController implements IStudentAccountController {

    @Autowired
    private IStudentAccountService studentAccountService;
    @Autowired
    private ICreationService creationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentAccount> getAll() {
        return studentAccountService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentAccount getByAccountNumber(@PathVariable(name = "id") Long accountNumber) {
        return studentAccountService.findByAccountNumber(accountNumber);
    }

    @PutMapping("/credit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction creditFunds(@RequestBody TransactionDTO transactionDTO) {
        return studentAccountService.creditFunds(transactionDTO);
    }

    @PutMapping("/debit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction debitFunds(@RequestBody TransactionDTO transactionDTO) {
        return studentAccountService.debitFunds(transactionDTO);
    }

    @PutMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Transaction transferFunds(@RequestBody TransactionDTO transactionDTO) {
        return studentAccountService.transferBetweenAccounts(transactionDTO);
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
        return studentAccountService.findTransactionBetween(accountNumber, startPoint, endPoint);
    }

    @PutMapping("/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StudentAccount createStudentAccount(@RequestBody CheckingAccountDTO checkingAccountDTO) {
        return creationService.newStudentAccount(checkingAccountDTO);
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public StudentAccount updateStudentAccount(@PathVariable(name = "id") Long accountNumber,
                                               @RequestBody CheckingAccountDTO checkingAccountDTO) {
        studentAccountService.findByAccountNumber(accountNumber);
        checkingAccountDTO.setAccountNumber(accountNumber);
        return creationService.newStudentAccount(checkingAccountDTO);
    }
}
