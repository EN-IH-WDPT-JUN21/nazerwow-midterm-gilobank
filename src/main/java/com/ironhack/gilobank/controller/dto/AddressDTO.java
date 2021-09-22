package com.ironhack.gilobank.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;

    private String houseNumber;
    private String flatNumber;

    private String street;
    private String town;
    private String city;
    private String postcode;

    public AddressDTO(String houseNumber, String street, String city, String postcode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
    }

    public AddressDTO(String houseNumber, String flatNumber, String street, String town, String city, String postcode) {
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
        this.street = street;
        this.town = town;
        this.city = city;
        this.postcode = postcode;
    }

}
