package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.repositories.AddressRepository;
import com.ironhack.gilobank.service.interfaces.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address findById(Long id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Address found with ID: " + id);
        }
        return addressOptional.get();
    }

    public void saveNewAddress(Address address) {
        addressRepository.save(address);
    }
}
