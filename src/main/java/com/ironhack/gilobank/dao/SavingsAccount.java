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
public class SavingsAccount extends Account {

    @NotNull
    @DecimalMin(value = "100.00", message = "Account cannot go below $100.00")
    @Digits(integer = 30, fraction = 2)
    private BigDecimal balance = new BigDecimal("100");

    @NotNull
    private BigDecimal minimumBalance = new BigDecimal("1000.00");

    @NotNull
    @DecimalMax(value = "0.5")
    @DecimalMin(value = "0")
    private BigDecimal interestRate = new BigDecimal("0.0025");


    public SavingsAccount(String secretKey, AccountHolder primaryHolder, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, balance);
        setBalance(balance);
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        setBalance(balance);
    }

    public SavingsAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        setBalance(balance);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    @Override
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance;
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
