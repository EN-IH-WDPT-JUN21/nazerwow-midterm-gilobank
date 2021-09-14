package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.repositories.CheckingAccountRepository;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CheckingAccountService implements ICheckingAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;


    public Optional<CheckingAccount> findByAccountNumber(Long accountNumber) {
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(accountNumber);
        if(!checkingAccount.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Checking Account found with Account Number: " + accountNumber);

        return checkingAccount;
    }

    public List<CheckingAccount> findAll() {
        return checkingAccountRepository.findAll();
    }
}
