package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckingAccount extends Account {


    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "checkingMonthlyMaintCurrency")),
            @AttributeOverride( name = "amount", column = @Column(name = "checkingMonthlyMaintenance"))})
    private Money monthlyMaintenanceFee = new Money(new BigDecimal("12.00"));

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "currency", column = @Column(name = "checkingMinCurrency")),
            @AttributeOverride( name = "amount", column = @Column(name = "checkingMinBalance"))})
    private Money minimumBalance = new Money(new BigDecimal("250.00"));

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, Money balance) {
        super(secretKey, primaryHolder, balance);
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, Money balance, Money monthlyMaintenanceFee, Money minimumBalance) {
        super(secretKey, primaryHolder, balance);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money monthlyMaintenanceFee, Money minimumBalance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money penaltyFee, LocalDate openDate, Status status, Money monthlyMaintenanceFee, Money minimumBalance) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }
}

