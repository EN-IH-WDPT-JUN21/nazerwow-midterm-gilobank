package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckingAccount extends Account{


    @NotNull
    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12");
    @NotNull
    private BigDecimal minimumBalance = new BigDecimal("250");

    public CheckingAccount(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
    }

    public CheckingAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
    }

    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
    }

    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        super(primaryHolder, secondaryHolder, balance);
    }

    public CheckingAccount(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(primaryHolder, secondaryHolder, balance);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }
}
