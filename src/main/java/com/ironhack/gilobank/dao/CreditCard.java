package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public CreditCard(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }
}
