package com.ironhack.gilobank.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyTransactionDTO {

    private BigDecimal amount;
    private Long creditAccountNumber;
    private String creditAccountSecretKey;
    private Long debitAccountNumber;
    private String debitAccountSecretKey;

}
