package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.CreditCard;

import java.util.List;
import java.util.Optional;

public interface ICreditCardController {

    List<CreditCard> getAll();
    Optional<CreditCard> getByAccountNumber(Long accountNumber);
}
