package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.SavingsAccount;

import java.util.List;
import java.util.Optional;

public interface ISavingsAccountController {

    List<SavingsAccount> getAll();

    Optional<SavingsAccount> getByAccountNumber(Long accountNumber);
}
