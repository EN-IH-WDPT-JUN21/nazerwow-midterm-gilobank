package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyTransactionDTO {

    private Money amount;

    private Long creditAccountNumber;

    @Size(min = 1, max = 28)
    private String creditAccountSecretKey;

    @Size(min = 1, max = 28)
    private Long debitAccountNumber;

    @Size(min = 1, max = 28)
    private String debitAccountSecretKey;


}
