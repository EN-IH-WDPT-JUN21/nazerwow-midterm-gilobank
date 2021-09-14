package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class SavingsAccount extends Account{

    @NotNull
    private BigDecimal minimumBalance;

    @NotNull
    @Max(100)
    private BigDecimal interestRate;

    public SavingsAccount(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(BigDecimal minimumBalance, BigDecimal interestRate) {
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, secondaryHolder, balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, BigDecimal balance, LocalDate openDate, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, balance, openDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, LocalDate openDate, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, secondaryHolder, balance, openDate);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(primaryHolder, balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(Long accountNumber, AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(accountNumber, primaryHolder, balance, penaltyFee, openDate, status);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }
}
