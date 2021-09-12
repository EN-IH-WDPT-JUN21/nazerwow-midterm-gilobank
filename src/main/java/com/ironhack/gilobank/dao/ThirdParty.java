package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class ThirdParty extends User{

    @NotNull
    private String name;

    @NotNull
    private String hashedKey;

    public ThirdParty(Long id, String username, String password, Role role, String name, String hashedKey) {
        super(id, username, password, role);
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public ThirdParty(String username, String password, Role role, String name, String hashedKey) {
        super(username, password, role);
        this.name = name;
        this.hashedKey = hashedKey;
    }
}
