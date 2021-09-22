package com.ironhack.gilobank.controller.dto;

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

    public LoginDetailsDTO(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

}
