package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.interfaces.ISavingsAccountController;
import com.ironhack.gilobank.dao.SavingsAccount;
import com.ironhack.gilobank.service.interfaces.ISavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account/saving")
public class SavingsAccountController implements ISavingsAccountController {

    @Autowired
    private ISavingsAccountService savingsAccountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getAll() {
        return savingsAccountService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<SavingsAccount> getByAccountNumber(@PathVariable(name="id") Long accountNumber) {
        return savingsAccountService.findByAccountNumber(accountNumber);
    }
}
