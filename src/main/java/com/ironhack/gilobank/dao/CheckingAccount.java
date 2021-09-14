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

    // Single Account - All args from super class
    public CheckingAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
    }
    // Joint Account - All args from super class
    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
    }

    // Only requires Both account holders and initial balance - All other fields set automatically
    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        super(primaryHolder, secondaryHolder, balance);
    }
    // Only requires 1 account holder,initial balance and Open Date - All other fields set automatically
    public CheckingAccount(AccountHolder primaryHolder, BigDecimal balance, LocalDate openDate) {
        super(primaryHolder, balance, openDate);
    }

    // Only requires Both account holders, initial balance and Open Date - All other fields set automatically
    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, LocalDate openDate) {
        super(primaryHolder, secondaryHolder, balance, openDate);
    }

    // Only requires Single Account Holder and Opening Balance
    public CheckingAccount(AccountHolder primaryHolder, BigDecimal balance) {
        super(primaryHolder, balance);
    }

    // "All arg" constructor - Joint Account
    public CheckingAccount(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder,
                           BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status,
                           BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    // "All Arg" Constructor - Single Account
    public CheckingAccount(Long accountNumber, AccountHolder primaryHolder,
                           BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status,
                           BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(accountNumber, primaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    // Joint Account Constructor with No AccountNumber Needed "all other args"
    public CheckingAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }

    // Single Account Constructor with No AccountNumber Needed "all other args"
    public CheckingAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }
}
