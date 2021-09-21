package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    private Long accountNumber;

    private String secretKey;

    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;

    private List<Transaction> transaction;

    private BigDecimal balance;

    private BigDecimal penaltyFee;

    private LocalDate openDate;

    private Status status;


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

    public AccountDTO(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
    }

    public AccountDTO(String secretKey, AccountHolder primaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
    }
}
