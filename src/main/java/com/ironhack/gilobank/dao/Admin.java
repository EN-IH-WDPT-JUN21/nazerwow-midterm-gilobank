package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.ADMIN;


    public Admin(LoginDetails loginDetails, String name) {
        super(loginDetails);
        this.name = name;
    }
}
