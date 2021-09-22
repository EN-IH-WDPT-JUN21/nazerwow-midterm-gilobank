package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.ADMIN;


    public Admin(String name) {
        this.name = name;
    }
}
