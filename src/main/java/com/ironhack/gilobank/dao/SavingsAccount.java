package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccount extends Account{

    @NotNull
    private BigDecimal minimumBalance;

    @NotNull
    private BigDecimal interestRate;

    public SavingsAccount(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

}
