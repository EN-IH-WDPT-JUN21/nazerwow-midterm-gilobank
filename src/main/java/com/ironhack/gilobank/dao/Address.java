package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String houseNumber;
    private String flatNumber;
    @NotNull
    private String street;
    private String town;
    @NotNull
    private String city;
    @NotNull
    private String postcode;

    @OneToMany(mappedBy = "primaryAddress", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<AccountHolder> primaryAddressAH;

    @OneToMany(mappedBy = "mailingAddress", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<AccountHolder> mailingAddressAH;

    public Address(String houseNumber, String street, String town, String city, String postcode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.town = town;
        this.city = city;
        this.postcode = postcode;
    }

    public Address(String houseNumber, String street, String city, String postcode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }


}
