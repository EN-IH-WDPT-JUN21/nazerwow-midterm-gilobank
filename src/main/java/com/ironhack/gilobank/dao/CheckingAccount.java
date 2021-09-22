package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
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
    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12.00");
    @NotNull
    private BigDecimal minimumBalance = new BigDecimal("250.00");

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, balance);
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, BigDecimal balance, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(secretKey, primaryHolder, balance);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }
}

