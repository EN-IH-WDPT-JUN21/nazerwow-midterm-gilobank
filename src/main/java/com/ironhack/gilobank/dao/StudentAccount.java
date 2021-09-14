package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class StudentAccount extends Account{

    public StudentAccount(Long accountNumber, AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(accountNumber, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
    }

    public StudentAccount() {
    }

    public StudentAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(primaryHolder, balance, penaltyFee, openDate, status);
    }

    public StudentAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
    }

    public StudentAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance) {
        super(primaryHolder, secondaryHolder, balance);
    }

    public StudentAccount(AccountHolder primaryHolder, BigDecimal balance, LocalDate openDate) {
        super(primaryHolder, balance, openDate);
    }

    public StudentAccount(AccountHolder primaryHolder, AccountHolder secondaryHolder, BigDecimal balance, LocalDate openDate) {
        super(primaryHolder, secondaryHolder, balance, openDate);
    }

    public StudentAccount(AccountHolder primaryHolder, BigDecimal balance) {
        super(primaryHolder, balance);
    }

    public StudentAccount(Long accountNumber, AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate openDate, Status status) {
        super(accountNumber, primaryHolder, balance, penaltyFee, openDate, status);
    }
}
