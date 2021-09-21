package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.SavingsAccount;

import java.util.List;

public interface ISavingsAccountController {

    List<SavingsAccount> getAll();

    SavingsAccount getByAccountNumber(Long accountNumber);
}
