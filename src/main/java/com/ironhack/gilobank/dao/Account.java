package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name="accnum", initialValue= 11223344, allocationSize=1)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accnum")
    private Long accountNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name="primaryHolder", referencedColumnName = "id")
    @JsonManagedReference
    private AccountHolder primaryHolder;

    @ManyToOne
    @JoinColumn(name="secondaryHolder", referencedColumnName = "id")
    @JsonManagedReference
    private AccountHolder secondaryHolder;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Transaction> transaction;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private BigDecimal penaltyFee = new BigDecimal("40.00");

    private LocalDate openDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;


    public Account(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.primaryHolder = primaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }

    public Account(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }

    public Account(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
    }

    public Account(AccountHolder primaryHolder, BigDecimal balance, LocalDate openDate) {
        this.primaryHolder = primaryHolder;
        this.balance = balance;
        this.openDate = openDate;
    }

    public Account(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, LocalDate openDate) {
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
        this.openDate = openDate;
    }

    public Account(AccountHolder primaryHolder, BigDecimal balance) {
        this.primaryHolder = primaryHolder;
        this.balance = balance;
    }

    public Account(Long accountNumber, AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.accountNumber = accountNumber;
        this.primaryHolder = primaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }

    public Account(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.accountNumber = accountNumber;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.balance = balance;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }

    public Money getBalanceAsMoney(){
        return new Money(balance);
    }

    public void credit(BigDecimal amount){
        this.balance = getBalanceAsMoney().increaseAmount(amount);
    }

    public void debit(BigDecimal amount){
        this.balance = getBalanceAsMoney().decreaseAmount(amount);
    }

}
