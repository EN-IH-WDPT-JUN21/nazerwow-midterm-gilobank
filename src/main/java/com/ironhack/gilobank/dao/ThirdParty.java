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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdParty extends User {

    @NotNull
    private String name;

    @NotNull
    private String hashedKey;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.THIRDPARTY;

    public ThirdParty(LoginDetails loginDetails, String name, String hashedKey) {
        super(loginDetails);
        this.name = name;
        this.hashedKey = hashedKey;
    }
}
