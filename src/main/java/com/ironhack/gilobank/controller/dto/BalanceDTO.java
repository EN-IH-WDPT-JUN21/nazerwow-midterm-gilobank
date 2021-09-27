package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BalanceDTO {

    private Long accountNumber;
    @Embedded
    private Money balance;


}
