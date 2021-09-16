package com.ironhack.gilobank.utils;

import com.ironhack.gilobank.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FraudDetection {

    @Autowired
    private TransactionRepository transactionRepository;


}
