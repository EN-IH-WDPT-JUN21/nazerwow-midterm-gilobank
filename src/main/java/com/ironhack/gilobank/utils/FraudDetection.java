package com.ironhack.gilobank.utils;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class FraudDetection {

    @Autowired
    private TransactionRepository transactionRepository;

    // Checks if account has had previous transactions - if not the payment is allowed
    // Utilises debits within 1 second method
    // Calculates the highest 24 Spending allowed
    // Utilises current 24 spend method to check against highest allowed
    public boolean fraudDetector(Account account, BigDecimal payment) {
        if (transactionRepository.findAll().isEmpty()) {
            return false;
        }
        if (debitsWithin1SecondLast24Hours(account)) {
            return true;
        }
        BigDecimal highestAllowedSpending = getHighestEver24Spend(account).multiply(new BigDecimal("1.50"));
        return highestAllowedSpending.compareTo(current24Spend(account, payment)) < 0;
    }

    // Checks if new transaction is within 1 second of instance where 2 transactions have happened within the same
    // second in the last 24 hours
    public boolean debitsWithin1SecondLast24Hours(Account account) {
        LocalDateTime timeOfNewTransaction = LocalDateTime.now();
        List<Timestamp> debitsGroupBySecond = transactionRepository.debitsWithin1Second(account);
        if (debitsGroupBySecond.isEmpty()) {
            return false;
        }
        LocalDateTime timeRangeStart = debitsGroupBySecond.get(debitsGroupBySecond.size() - 1).toLocalDateTime();
        LocalDateTime timeRangeEnd = debitsGroupBySecond.get(debitsGroupBySecond.size() - 1).toLocalDateTime().plusSeconds(1);
        return timeOfNewTransaction.isAfter(timeRangeStart) && timeOfNewTransaction.isBefore(timeRangeEnd);
    }

    // Gets history of highest 24 spend over account lifetime
    public BigDecimal getHighestEver24Spend(Account account) {
        List<BigDecimal> listOfHistoricTotals = transactionRepository.historicDailyTotals(account);
        Collections.sort(listOfHistoricTotals);
        return listOfHistoricTotals.get(listOfHistoricTotals.size() - 1);
    }

    // Returns total of new payment + spending in last 4 hours
    public BigDecimal current24Spend(Account account, BigDecimal payment) {
        BigDecimal totalLast24Hours = transactionRepository.totalOfAllDebitsFromLast24Hours(account);
        return totalLast24Hours.add(payment);
    }


}
