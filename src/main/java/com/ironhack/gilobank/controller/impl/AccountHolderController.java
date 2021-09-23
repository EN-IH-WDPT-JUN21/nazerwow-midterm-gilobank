package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.interfaces.IAccountHolderController;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.service.interfaces.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/accholder")
public class AccountHolderController implements IAccountHolderController {

    @Autowired
    private IAccountHolderService accountHolderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAll() {
        return accountHolderService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder getById(@PathVariable(name = "id") Long id) {
        return accountHolderService.findById(id);
    }


}
