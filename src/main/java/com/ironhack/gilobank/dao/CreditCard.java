package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCard extends Account{


    @NotNull
    private BigDecimal creditLimit;

    @NotNull
    private BigDecimal interestRate;

    public CreditCard(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(BigDecimal creditLimit, BigDecimal interestRate) {
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, secondaryHolder, balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryHolder, BigDecimal balance, LocalDate openDate, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, balance, openDate);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, LocalDate openDate, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, secondaryHolder, balance, openDate);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(AccountHolder primaryHolder, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(Long accountNumber, AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(accountNumber, primaryHolder, balance, penaltyFee, openDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }
}
