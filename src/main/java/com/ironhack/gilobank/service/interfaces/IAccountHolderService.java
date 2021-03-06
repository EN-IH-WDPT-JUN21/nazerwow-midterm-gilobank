package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.AccountHolder;

import java.util.List;

public interface IAccountHolderService {

    List<AccountHolder> findAll();

    AccountHolder findById(Long id);

    void saveNewAccountHolder(AccountHolder accountHolder);
}
