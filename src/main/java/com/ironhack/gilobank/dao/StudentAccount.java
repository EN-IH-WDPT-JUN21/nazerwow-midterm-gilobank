package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.utils.Money;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
public class StudentAccount extends Account {

    public StudentAccount() {
    }

    public StudentAccount(Long accountNumber, String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, List<Transaction> transaction, Money balance, Money penaltyFee, LocalDate openDate, Status status) {
        super(accountNumber, secretKey, primaryHolder, secondaryHolder, transaction, balance, penaltyFee, openDate, status);
    }

    public StudentAccount(String secretKey, AccountHolder primaryHolder, Money balance) {
        super(secretKey, primaryHolder, balance);
    }

    public StudentAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance) {
        super(secretKey, primaryHolder, secondaryHolder, balance);
    }

    public StudentAccount(String secretKey, AccountHolder primaryHolder, AccountHolder secondaryHolder, Money balance, Money penaltyFee, LocalDate openDate, Status status) {
        super(secretKey, primaryHolder, secondaryHolder, balance, penaltyFee, openDate, status);
    }
}
