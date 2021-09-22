package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccountDTO {

    private Long accountNumber;

    @NotNull
    private String secretKey;

    @NotNull
    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;


    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for Amount")
    private BigDecimal balance;

    private BigDecimal penaltyFee;

    private LocalDate openDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private BigDecimal monthlyMaintenanceFee;

    private BigDecimal minimumBalance;


    public CheckingAccountDTO(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CheckingAccountDTO(Long accountNumber, AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.accountNumber = accountNumber;
        this.primaryHolder = primaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }

    public CheckingAccountDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
    }

    public CheckingAccountDTO(String secretKey, AccountHolder primaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
    }

    public CheckingAccountDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status, BigDecimal monthlyMaintenanceFee, BigDecimal minimumBalance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
        this.minimumBalance = minimumBalance;
    }
}
