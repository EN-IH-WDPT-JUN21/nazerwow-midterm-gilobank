package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Status;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
public class StudentAccount extends Account{

    public StudentAccount(AccountHolder primaryHolder, BigDecimal balance, BigDecimal penaltyFee, LocalDate dateOfBirth, Status status) {
        super(primaryHolder, balance, penaltyFee, dateOfBirth, status);
    }
}
