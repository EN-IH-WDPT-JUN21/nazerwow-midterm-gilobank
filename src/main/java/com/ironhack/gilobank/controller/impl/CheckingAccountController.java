package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.interfaces.ICheckingAccountController;
import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account/checking")
public class CheckingAccountController implements ICheckingAccountController {

    @Autowired
    private ICheckingAccountService checkingAccountService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CheckingAccount> getByAccountNumber(@PathVariable(name="id") Long accountNumber) {
        return checkingAccountService.findByAccountNumber(accountNumber);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> getAll() {
        return checkingAccountService.findAll();
    }
}
