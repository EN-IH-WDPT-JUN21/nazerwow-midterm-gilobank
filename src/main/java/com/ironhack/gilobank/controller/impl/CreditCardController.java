package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.interfaces.ICreditCardController;
import com.ironhack.gilobank.dao.CreditCard;
import com.ironhack.gilobank.service.interfaces.ICreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account/creditcard")
public class CreditCardController implements ICreditCardController {

    @Autowired
    private ICreditCardService creditCardService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> getAll() {
        return creditCardService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<CreditCard> getByAccountNumber(@PathVariable(name="id") Long accountNumber) {
        return creditCardService.findByAccountNumber(accountNumber);
    }
}
