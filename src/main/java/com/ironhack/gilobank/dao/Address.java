package com.ironhack.gilobank.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    private String houseNumber;
    @Size(min = 1, max = 10)
    private String flatNumber;
    @NotNull
    @Size(min = 1, max = 255)
    private String street;
    @Size(min = 1, max = 255)
    private String town;
    @NotNull
    @Size(min = 1, max = 255)
    private String city;
    @NotNull
    @Size(min = 4, max = 8)
    private String postcode;

    @OneToMany(mappedBy = "primaryAddress", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AccountHolder> primaryAddressAH;

    @OneToMany(mappedBy = "mailingAddress", fetch = FetchType.LAZY)
    @JsonIgnore
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
