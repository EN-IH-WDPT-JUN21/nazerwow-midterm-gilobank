package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class SavingsAccount extends Account {

    @NotNull
    @Min(100)
    private BigDecimal minimumBalance = new BigDecimal("1000.00");

    @NotNull
    private BigDecimal interestRate = new BigDecimal("0.0025");


    public SavingsAccount(String secretKey, AccountHolder primaryHolder, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, balance);
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }
}
