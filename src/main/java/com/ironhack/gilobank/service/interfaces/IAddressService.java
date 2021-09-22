package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.Address;

public interface IAddressService {
    Address findById(Long id);
    void saveNewAddress(Address address);
}
