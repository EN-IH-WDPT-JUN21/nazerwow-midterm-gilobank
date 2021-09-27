package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyTransactionDTO {

    private Money amount;

    private Long creditAccountNumber;

    private String creditAccountSecretKey;

    private Long debitAccountNumber;

    private String debitAccountSecretKey;


}
