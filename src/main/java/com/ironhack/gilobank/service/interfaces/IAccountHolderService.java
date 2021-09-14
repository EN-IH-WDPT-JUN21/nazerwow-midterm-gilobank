package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.AccountHolder;

import java.util.List;
import java.util.Optional;

public interface IAccountHolderService {

    List<AccountHolder> findAll();
    Optional<AccountHolder> findById(Long id);

}
