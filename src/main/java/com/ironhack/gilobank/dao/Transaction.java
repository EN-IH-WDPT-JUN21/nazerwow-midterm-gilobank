package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountNumber")
    @JsonManagedReference
    private Account account;

    @Size(min = 1, max = 255)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "transactionCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "transactionAmount"))})
    private Money amount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "BalanceCurrency")),
            @AttributeOverride(name = "amount", column = @Column(name = "BalanceAmount"))})
    private Money balanceAfterTransaction;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime timeOfTrns = LocalDateTime.now();

    public Transaction(Account account) {
        this.account = account;
    }

    public Transaction(Account account, String name, Money amount, Money balanceAfterTransaction) {
        this.account = account;
        this.name = name;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public Transaction(Account account, String name, Money amount, Money balanceAfterTransaction, LocalDateTime timeOfTrns) {
        this.account = account;
        this.name = name;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.timeOfTrns = timeOfTrns;
    }

    public Transaction(Account account, String name, Money amount, Money balanceAfterTransaction, TransactionType type, LocalDateTime timeOfTrns) {
        this.account = account;
        this.name = name;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.type = type;
        this.timeOfTrns = timeOfTrns;
    }

    public Transaction(Account account, String name, Money amount, Money balanceAfterTransaction, TransactionType type) {
        this.account = account;
        this.name = name;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.type = type;
    }
}
