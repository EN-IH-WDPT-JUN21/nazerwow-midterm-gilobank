package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ironhack.gilobank.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder extends User {

    @Enumerated(EnumType.STRING)
    private Role role = Role.ACCOUNTHOLDER;

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "primary_address", referencedColumnName = "id")
    private Address primaryAddress;

    @ManyToOne
    @JoinColumn(name = "mailing_address", referencedColumnName = "id")
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryHolder", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Account> accountPrimaryHolder;

    @OneToMany(mappedBy = "secondaryHolder", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Account> accountSecondaryHolder;

    public AccountHolder(String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public AccountHolder(String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress) {
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }
}
