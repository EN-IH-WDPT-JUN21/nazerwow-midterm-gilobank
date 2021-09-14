package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.CheckingAccount;

import java.util.List;
import java.util.Optional;

public interface ICheckingAccountController {

    Optional<CheckingAccount> getByAccountNumber(Long accountNumber);
    List<CheckingAccount> getAll();
}
