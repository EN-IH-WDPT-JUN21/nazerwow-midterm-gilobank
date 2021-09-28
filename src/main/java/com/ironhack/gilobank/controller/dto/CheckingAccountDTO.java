package com.ironhack.gilobank.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccountDTO {

    private Long accountNumber;

    @Size(min = 1, max = 255)
    private String secretKey;

    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;


    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for Amount")
    private BigDecimal balance;

    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for penalty fee")
    private BigDecimal penaltyFee = new BigDecimal("40.00");

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for monthly maintenance fee")
    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12.00");

    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for minimum balance")
    private BigDecimal minimumBalance = new BigDecimal("250.00");


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

    public CheckingAccountDTO(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.balance = balance;
    }

    public CheckingAccountDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
    }
}
