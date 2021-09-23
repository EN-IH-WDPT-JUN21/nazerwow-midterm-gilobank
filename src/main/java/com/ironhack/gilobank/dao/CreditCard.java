package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
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
public class CreditCard extends Account {

    @NotNull
    @DecimalMax(value = "0.00")
    @Digits(integer = 30, fraction = 2)
    private BigDecimal balance = new BigDecimal("-100.00");

    @NotNull
    @DecimalMax(value = "-100.00")
    @DecimalMin(value = "-100000.00")
    @Digits(integer = 8, fraction = 2, message = "Max digits 8, Max fraction 2, Reminder: start with '-'")
    private BigDecimal creditLimit = new BigDecimal("-100.00");

    @NotNull
    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.1")
    private BigDecimal interestRate = new BigDecimal("0.20");

    public CreditCard(String secretKey, AccountHolder primaryHolder, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        setBalance(balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        setBalance(balance);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance;
    }

    public BigDecimal remainingBalance() {
        return getCreditLimit().subtract(getBalance()).abs();
    }

    @Override
    public Money getBalanceAsMoney() {
        return new Money(getBalance());
    }

    @Override
    public void credit(BigDecimal amount) {
        setBalance(getBalanceAsMoney().increaseAmount(amount));
    }

    @Override
    public void debit(BigDecimal amount) {
        setBalance(getBalanceAsMoney().decreaseAmount(amount));
    }
}
