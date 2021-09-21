package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ironhack.gilobank.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private LoginDetails loginDetails;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }
}
