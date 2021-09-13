package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Min(1223344)
    private Long accountNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name="primaryHolder", referencedColumnName = "id")
    private AccountHolder primaryHolder;

    @ManyToOne
    @JoinColumn(name="secondaryHolder", referencedColumnName = "id")
    private AccountHolder secondaryHolder;

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
}
