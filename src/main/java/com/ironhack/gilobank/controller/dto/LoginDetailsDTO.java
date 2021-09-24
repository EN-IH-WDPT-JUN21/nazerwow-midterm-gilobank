package com.ironhack.gilobank.controller.dto;

import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Admin;
import com.ironhack.gilobank.dao.ThirdParty;
import com.ironhack.gilobank.dao.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDetailsDTO {

    private Long id;

    private String username;
    private String password;


    private User user;
    private AccountHolder accountHolder;
    private Admin admin;
    private ThirdParty thirdParty;

    public LoginDetailsDTO(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public LoginDetailsDTO(String username, String password, AccountHolder accountHolder) {
        this.username = username;
        this.password = password;
        this.accountHolder = accountHolder;
    }

    public LoginDetailsDTO(Long id, String username, String password, User user) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public LoginDetailsDTO(Long id, String username, String password, AccountHolder accountHolder) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accountHolder = accountHolder;
    }


}
