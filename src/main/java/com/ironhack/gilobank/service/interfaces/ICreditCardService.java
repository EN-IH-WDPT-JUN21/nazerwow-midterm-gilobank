package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.CreditCard;

import java.util.List;
import java.util.Optional;

public interface ICreditCardService {

    List<CreditCard> findAll();
    Optional<CreditCard> findByAccountNumber(Long accountNumber);

}
