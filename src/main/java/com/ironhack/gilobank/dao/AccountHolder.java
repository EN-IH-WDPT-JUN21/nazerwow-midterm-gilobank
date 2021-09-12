package com.ironhack.gilobank.dao;

import com.ironhack.gilobank.enums.Role;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder extends User{

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "primary_address", referencedColumnName = "id")
    private Address primaryAddress;
    @ManyToOne
    @JoinColumn(name = "mailing_address", referencedColumnName = "id")
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryHolder", fetch = FetchType.LAZY)
    private Set<Account> accountPrimaryHolder;

    @OneToMany(mappedBy = "secondaryHolder", fetch = FetchType.LAZY)
    private Set<Account> accountSecondaryHolder;

    public AccountHolder(Long id, String username, String password, Role role, String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress) {
        super(id, username, password, role);
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolder(String username, String password, Role role, String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress) {
        super(username, password, role);
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolder(Long id, String username, String password, Role role, String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(id, username, password, role);
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public AccountHolder(String username, String password, Role role, String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(username, password, role);
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }
}
