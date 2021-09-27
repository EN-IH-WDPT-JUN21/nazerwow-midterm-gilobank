package com.ironhack.gilobank.controller.dto;


import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

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