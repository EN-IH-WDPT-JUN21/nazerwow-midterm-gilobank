package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavingsAccountDTO {

    private Long accountNumber;

    private String secretKey;

    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;

    @DecimalMin(value = "100.00", message = "Account cannot go below $100.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for Amount")
    private BigDecimal balance;

    private BigDecimal penaltyFee;

    private LocalDate openDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private BigDecimal minimumBalance = new BigDecimal("1000.00");

    @DecimalMax(value = "0.5")
    @DecimalMin(value = "0")
    private BigDecimal interestRate = new BigDecimal("0.0025");

    public SavingsAccountDTO(String secretKey, AccountHolder primaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
    }

    public SavingsAccountDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
    }

    public SavingsAccountDTO(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.balance = balance;
    }

    public SavingsAccountDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
    }
}
