package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Admin extends User{

    @NotNull
    private String name;

    public Admin(Long id, String username, String password, Role role, String name) {
        super(id, username, password, role);
        this.name = name;
    }

    public Admin(String username, String password, Role role, String name) {
        super(username, password, role);
        this.name = name;
    }
}
