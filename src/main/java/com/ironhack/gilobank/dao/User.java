package com.ironhack.gilobank.dao;

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
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="loginDetails")
    private LoginDetails loginDetails;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }
}
