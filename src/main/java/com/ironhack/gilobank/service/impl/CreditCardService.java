package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.CreditCard;
import com.ironhack.gilobank.repositories.CreditCardRepository;
import com.ironhack.gilobank.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService implements ICreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public List<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    public Optional<CreditCard> findByAccountNumber(Long accountNumber) {
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(accountNumber);
        if(optionalCreditCard.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Credit Card Found with Account Number :" + accountNumber);

        return optionalCreditCard;
    }
}
