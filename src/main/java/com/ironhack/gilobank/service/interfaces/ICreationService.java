package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.dao.AccountHolder;
import com.ironhack.gilobank.dao.Address;
import com.ironhack.gilobank.dao.LoginDetails;

public interface ICreationService {

    Address newAddress(AddressDTO addressDTO);
    AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO);
    LoginDetails newLoginDetails(LoginDetailsDTO loginDetailsDTO);
}
