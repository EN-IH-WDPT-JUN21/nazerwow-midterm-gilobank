package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.dao.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private String secretKey;

    private AccountHolder primaryHolder;

    private AccountHolder secondaryHolder;

    @DecimalMin(value = "0.00")
    @Digits(integer = 30, fraction = 2, message = "Error: Incorrect format for Amount")
    private BigDecimal balance;

    public AccountDTO(String secretKey, AccountHolder primaryHolder) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
    }

    public AccountDTO(String secretKey, AccountHolder primaryHolder, BigDecimal balance) {
        this.secretKey = secretKey;
        this.primaryHolder = primaryHolder;
        this.balance = balance;
    }


}
