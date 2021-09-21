package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    private Long creditAccountNumber;

    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2)
    private BigDecimal amount;

    private Long debitAccountNumber;

    private LocalDateTime timeOfTrns;

    @Enumerated(EnumType.STRING)
    private TransactionType type;


    public TransactionDTO(Long creditAccountNumber, BigDecimal amount, TransactionType type) {
        this.creditAccountNumber = creditAccountNumber;
        this.amount = amount;
        this.type = type;
    }

    public TransactionDTO(BigDecimal amount, Long debitAccountNumber, TransactionType type) {
        this.amount = amount;
        this.debitAccountNumber = debitAccountNumber;
        this.type = type;
    }

    public TransactionDTO(Long creditAccountNumber, BigDecimal amount, Long debitAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
        this.amount = amount;
        this.debitAccountNumber = debitAccountNumber;
    }

    public TransactionDTO(Long creditAccountNumber, BigDecimal amount, Long debitAccountNumber, LocalDateTime timeOfTrns, TransactionType type) {
        this.creditAccountNumber = creditAccountNumber;
        this.amount = amount;
        this.debitAccountNumber = debitAccountNumber;
        this.timeOfTrns = timeOfTrns;
        this.type = type;
    }
}
