package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.repositories.AccountHolderRepository;
import com.ironhack.gilobank.service.interfaces.IAccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderService implements IAccountHolderService {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    public List<AccountHolder> findAll() {
        return accountHolderRepository.findAll();
    }


    public Optional<AccountHolder> findById(Long id) {
        Optional<AccountHolder> accountHolder = accountHolderRepository.findById(id);
        if(!accountHolder.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Account Holder found with id: " + id);

        return accountHolder;
    }


}
