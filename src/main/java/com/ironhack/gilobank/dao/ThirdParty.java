package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdParty extends User {

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String hashedKey;

    @Enumerated(EnumType.STRING)
    private final Role role = Role.THIRDPARTY;

    public ThirdParty(Long id, LoginDetails loginDetails, Role role, String name, String hashedKey) {
        super(id, loginDetails, role);
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public ThirdParty(LoginDetails loginDetails, String name, String hashedKey) {
        super(loginDetails);
        this.name = name;
        this.hashedKey = hashedKey;
    }
}
