package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class SavingsAccount extends Account {

    @NotNull
//    @DecimalMin(value = "100.00", message = "Account cannot go below $100.00")
//    @Digits(integer = 30, fraction = 2)
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "savingsCurrency")),
            @AttributeOverride( name = "amount", column = @Column(name = "savingsBalance"))})
    private Money balance = new Money(new BigDecimal("100"));

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "savingsMinCurrency")),
            @AttributeOverride( name = "amount", column = @Column(name = "savingsMinBalance"))})
    private Money minimumBalance = new Money(new BigDecimal("1000.00"));

    @NotNull
    @DecimalMax(value = "0.5")
    @DecimalMin(value = "0")
    private BigDecimal interestRate = new BigDecimal("0.0025");


    public SavingsAccount(String secretKey, AccountHolder primaryHolder, Money balance, Money minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, Money balance) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        setBalance(balance);
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money penaltyFee, LocalDate openDate, Status status, Money minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    @Override
    public void setBalance(Money balance) {
        this.balance = balance;
    }

    @Override
    public Money getBalance() {
        return this.balance;
    }

}
