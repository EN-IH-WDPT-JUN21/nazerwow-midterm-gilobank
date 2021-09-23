package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "accnum", initialValue = 11223344, allocationSize = 1)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accnum")
    private Long accountNumber;

    @NotNull
    private String secretKey;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "primaryHolder", referencedColumnName = "id")
    @JsonIgnore
    @Embedded
    private AccountHolder primaryHolder;

    @ManyToOne
    @JoinColumn(name = "secondaryHolder", referencedColumnName = "id")
    @JsonIgnore
    @Embedded
    private AccountHolder secondaryHolder;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    @Embedded
    private List<Transaction> transaction;

    @NotNull
    @DecimalMin(value = "0.00", groups = {CheckingAccount.class, SavingsAccount.class, StudentAccount.class})
    @Digits(integer = 30, fraction = 2)
    private BigDecimal balance = new BigDecimal("0.00");

    @NotNull
    private BigDecimal penaltyFee = new BigDecimal("40.00");

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate openDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;


    public Account(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        setBalance(balance);
    }

    public Account(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        setBalance(balance);
    }

    public Account(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, LocalDate openDate) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        setBalance(balance);
        this.openDate = openDate;
    }

    public Account(String secretKey, AccountHolder primaryHolder, BigDecimal balance, LocalDate openDate) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        setBalance(balance);
        this.openDate = openDate;
    }

    public Account(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        setBalance(balance);
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }

    public Account(String secretKey, AccountHolder primaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
    }

    public Account(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
    }

    public Account(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, LocalDate openDate) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.openDate = openDate;
    }

    public Account(String secretKey, AccountHolder primaryHolder, LocalDate openDate) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.openDate = openDate;
    }

    public Account(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.secondaryHolder = secondaryHolder;
        this.penaltyFee = penaltyFee;
        this.openDate = openDate;
        this.status = status;
    }


    public Money getBalanceAsMoney() {
        return new Money(getBalance());
    }

    public void credit(BigDecimal amount) {
        setBalance(getBalanceAsMoney().increaseAmount(amount));
    }

    public void debit(BigDecimal amount) {
        setBalance(getBalanceAsMoney().decreaseAmount(amount));
    }

    public void freezeAccount() {
        this.status = Status.FROZEN;
    }

    public void unfreezeAccount() {
        this.status = Status.ACTIVE;
    }

}
