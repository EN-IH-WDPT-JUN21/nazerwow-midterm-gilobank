package com.ironhack.gilobank.controller.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    private Long accountNumber;

    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;

    private List<Transaction> transaction;

    private BigDecimal balance;

    private BigDecimal penaltyFee;

    private LocalDate openDate = LocalDate.now();

    private Status status = Status.ACTIVE;


    public AccountDTO(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountDTO(Long accountNumber, AccountHolder primaryHolder, List<Transaction> transaction, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.accountNumber = accountNumber;
        this.primaryHolder = primaryHolder;
        this.transaction = transaction;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }
}
