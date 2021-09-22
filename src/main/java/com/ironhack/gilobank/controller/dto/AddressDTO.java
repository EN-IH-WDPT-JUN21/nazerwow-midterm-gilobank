package com.ironhack.gilobank.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}
