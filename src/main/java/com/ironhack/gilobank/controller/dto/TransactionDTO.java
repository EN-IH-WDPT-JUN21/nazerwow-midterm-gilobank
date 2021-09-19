package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    private Long creditAccountNumber;

    private BigDecimal amount;

    private Long debitAccountNumber;

    private LocalDateTime timeOfTrns;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Long accountNumber;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    public TransactionDTO(Long creditAccountNumber, BigDecimal amount) {
        this.creditAccountNumber = creditAccountNumber;
        this.amount = amount;
    }

    public TransactionDTO(BigDecimal amount, Long debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
        this.amount = amount;
    }

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

    public TransactionDTO(Long accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.accountNumber = accountNumber;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public TransactionDTO(BigDecimal amount, LocalDateTime timeOfTrns, TransactionType type) {
        this.amount = amount;
        this.timeOfTrns = timeOfTrns;
        this.type = type;
    }
}
