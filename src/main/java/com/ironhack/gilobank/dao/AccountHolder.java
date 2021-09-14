package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.gilobank.enums.Role;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder extends User{

    @Id
    @GeneratedValue
    private Long id;

    private Role role = Role.ACCOUNTHOLDER;

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
    @JsonManagedReference
    private Address primaryAddress;

    @ManyToOne
    @JoinColumn(name = "mailing_address", referencedColumnName = "id")
    @JsonManagedReference
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryHolder", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Account> accountPrimaryHolder;

    @OneToMany(mappedBy = "secondaryHolder", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Account> accountSecondaryHolder;

    public AccountHolder(LoginDetails loginDetails, String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(loginDetails);
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }
}
