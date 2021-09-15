package com.ironhack.gilobank.controller.dto;


import com.ironhack.gilobank.dao.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO {

    private Long creditAccountNumber;

    private BigDecimal amount;

    private Long debitAccountNumber;

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
}
