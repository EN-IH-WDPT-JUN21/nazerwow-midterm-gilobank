package com.ironhack.gilobank.controller.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO {

    private Long accountNumber;

    private String secretKey;

    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;

    @DecimalMax(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for Amount")
    private BigDecimal balance;

    private BigDecimal penaltyFee;

    private LocalDate openDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @DecimalMax(value = "-100.00")
    @DecimalMin(value = "-100000.00")
    @Digits(integer = 8, fraction = 2, message = "Max digits 8, Max fraction 2, Reminder: start with '-'")
    private BigDecimal creditLimit = new BigDecimal("100.00");

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.1")
    private BigDecimal interestRate = new BigDecimal("0.20");

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
    }

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, BigDecimal creditLimit) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.creditLimit = creditLimit;
    }

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal creditLimit) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.creditLimit = creditLimit;
    }
}
