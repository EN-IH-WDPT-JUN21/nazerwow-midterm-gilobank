package com.ironhack.gilobank.utils;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class FraudDetection {

    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal current24Spend(Account account, BigDecimal payment){
        BigDecimal totalLast24Hours = transactionRepository.totalOfAllDebitsFromLast24Hours(account);
        return totalLast24Hours.add(payment);
    }

    public BigDecimal getHighestEver24Spend(Account account){
        List<BigDecimal> listOfHistoricTotals = transactionRepository.historicDailyTotals(account);
        Collections.sort(listOfHistoricTotals);
        return listOfHistoricTotals.get(listOfHistoricTotals.size() - 1);
    }

    public boolean fraudDetector(Account account, BigDecimal payment){
        if(transactionRepository.findAll().isEmpty()){
            return false;
        }
        BigDecimal highestAllowedSpending = getHighestEver24Spend(account).multiply(new BigDecimal("1.50"));
        if(highestAllowedSpending.compareTo(current24Spend(account, payment)) < 0){
            return true;
        }
        return false;
    }
}
