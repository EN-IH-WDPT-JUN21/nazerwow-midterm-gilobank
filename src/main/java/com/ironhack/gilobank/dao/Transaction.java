package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trans_seq")
//    @GenericGenerator(
//            name = "trans_seq",
//            strategy = "com.ironhack.gilobank.utils.AccNumPreFixedSequenceIdGenerator",
//            parameters = {
//                    @Parameter(name = AccNumPreFixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
//                    @Parameter(name = AccNumPreFixedSequenceIdGenerator.CODE_NUMBER_SEPARATOR_PARAMETER, value = "_"),
//                    @Parameter(name = AccNumPreFixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")})
    private Long id;

    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName = "accountNumber")
    @JsonManagedReference
    private Account account;
    private String name;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private LocalDate date = LocalDate.now();

    public Transaction(Account account) {
        this.account = account;
    }

    public Transaction(Account account, String name, BigDecimal amount, BigDecimal balanceAfterTransaction) {
        this.account = account;
        this.name = name;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public Transaction(Account account, String name, BigDecimal amount, BigDecimal balanceAfterTransaction, LocalDate date) {
        this.account = account;
        this.name = name;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.date = date;
    }
}
