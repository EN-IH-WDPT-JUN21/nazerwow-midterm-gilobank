package com.ironhack.gilobank.dao;

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
@Embeddable
public class LoginDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @Embedded
    private User user;

    public LoginDetails(String username, String password, User user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }
}
