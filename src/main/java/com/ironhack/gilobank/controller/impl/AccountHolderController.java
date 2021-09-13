package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.interfaces.IAccountHolderController;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.repositories.AccountHolderRepository;
import com.ironhack.gilobank.repositories.AddressRepository;
import com.ironhack.gilobank.repositories.LoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accholder")
public class AccountHolderController implements IAccountHolderController {

    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<AccountHolder> getAccountHolderById(@PathVariable(name="id") Long id) {
        return accountHolderRepository.findById(id);
    }

}
