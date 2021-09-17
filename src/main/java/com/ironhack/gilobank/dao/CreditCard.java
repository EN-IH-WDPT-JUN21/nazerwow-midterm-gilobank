package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCard extends Account {


    @NotNull
    private BigDecimal creditLimit;

    @NotNull
    private BigDecimal interestRate;

    public CreditCard(String secretKey, AccountHolder primaryHolder, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }
}
