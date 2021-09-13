package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.AccountHolder;

import java.util.List;
import java.util.Optional;

public interface IAccountHolderController {

    List<AccountHolder> getAllAccountHolders();
    Optional<AccountHolder> getAccountHolderById(Long id);
}
