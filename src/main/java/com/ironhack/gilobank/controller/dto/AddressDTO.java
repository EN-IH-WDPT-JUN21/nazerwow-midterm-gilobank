package com.ironhack.gilobank.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id;

    @Size(min = 1, max = 10)
    private String houseNumber;
    @Size(min = 1, max = 10)
    private String flatNumber;

    @Size(min = 1, max = 255)
    private String street;
    @Size(min = 1, max = 255)
    private String town;
    @Size(min = 1, max = 255)
    private String city;
    @Size(min = 4, max = 8)
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
