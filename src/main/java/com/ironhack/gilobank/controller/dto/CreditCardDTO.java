package com.ironhack.gilobank.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private Money balance;

    private Money penaltyFee;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @DecimalMax(value = "-100.00")
    @DecimalMin(value = "-100000.00")
    @Digits(integer = 8, fraction = 2, message = "Max digits 8, Max fraction 2, Reminder: start with '-'")
    private Money creditLimit = new Money(new BigDecimal("-100.00"));

    @DecimalMax(value = "1.00")
    @DecimalMin(value = "0.1")
    private BigDecimal interestRate = new BigDecimal("0.20");

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
    }

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, Money creditLimit) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.creditLimit = creditLimit;
    }

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money creditLimit) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.creditLimit = creditLimit;
    }

    public CreditCardDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money penaltyFee, LocalDate openDate, Status status, Money creditLimit, BigDecimal interestRate) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }
}
