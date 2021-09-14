package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.CheckingAccount;

import java.util.List;
import java.util.Optional;

public interface ICheckingAccountService {

    Optional<CheckingAccount> findByAccountNumber(Long accountNumber);
    List<CheckingAccount> findAll();


}
