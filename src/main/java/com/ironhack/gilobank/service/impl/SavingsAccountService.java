package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.SavingsAccount;
import com.ironhack.gilobank.repositories.SavingsAccountRepository;
import com.ironhack.gilobank.service.interfaces.ISavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SavingsAccountService implements ISavingsAccountService {

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    public List<SavingsAccount> findAll() {
        return savingsAccountRepository.findAll();
    }

    @Override
    public Optional<SavingsAccount> findByAccountNumber(Long accountNumber) {
        Optional<SavingsAccount> optionalSavingsAccount = savingsAccountRepository.findById(accountNumber);
        if (!optionalSavingsAccount.isPresent())
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No Savings Account Found with Account Number: " + accountNumber);
        return optionalSavingsAccount;
    }
}
