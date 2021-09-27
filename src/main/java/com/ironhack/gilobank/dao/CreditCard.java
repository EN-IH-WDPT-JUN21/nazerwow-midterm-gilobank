package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CreditCard extends Account {

    @NotNull
//    @DecimalMax(value = "0.00")
//    @Digits(integer = 30, fraction = 2)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "creditCardCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "creditCardBalance"))})
    private Money balance = new Money(new BigDecimal("-100.00"));

    @NotNull
//    @DecimalMax(value = "-100.00")
//    @DecimalMin(value = "-100000.00")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "creditLimitCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "CreditLimitAmount"))})
    private Money creditLimit = new Money(new BigDecimal("-100.00"));

    @NotNull
    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.1")
    private BigDecimal interestRate = new BigDecimal("0.20");

    public CreditCard(String secretKey, AccountHolder primaryHolder, Money balance, Money creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        setBalance(balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money penaltyFee, LocalDate openDate, Status status, Money creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        setBalance(balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, Money balance) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
    }

    @Override
    public void setBalance(Money balance) {
        this.balance = balance;
    }

    @Override
    public Money getBalance() {
        return this.balance;
    }

    public Money remainingBalance() {
        return new Money(getCreditLimit().decreaseAmount(getBalance()).abs());
    }
}
