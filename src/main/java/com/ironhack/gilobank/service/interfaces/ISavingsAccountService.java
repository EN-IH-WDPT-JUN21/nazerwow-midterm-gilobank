package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.SavingsAccount;

import java.util.List;
import java.util.Optional;

public interface ISavingsAccountService {

    List<SavingsAccount> findAll();

    Optional<SavingsAccount> findByAccountNumber(Long accountNumber);
}
