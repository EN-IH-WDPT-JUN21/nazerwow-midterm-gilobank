package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.AccountHolder;

import java.util.List;

public interface IAccountHolderController {

    List<AccountHolder> getAll();

    AccountHolder getById(Long id);

}
