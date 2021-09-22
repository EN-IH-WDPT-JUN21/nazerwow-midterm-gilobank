package com.ironhack.gilobank.controller.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Address;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolderDTO {

    private Long id;

    private String firstName;

    private String surname;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    private Address primaryAddress;

    private Address mailingAddress;

    @Enumerated(EnumType.STRING)
    private Role role;

    public AccountHolderDTO(String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress) {
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolderDTO(String firstName, String surname, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }


}
