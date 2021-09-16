package com.ironhack.gilobank.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
