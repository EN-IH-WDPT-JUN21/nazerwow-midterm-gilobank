package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.dao.*;

import java.util.List;

public interface IAdminService {

    Admin findById(Long id);

    List<Admin> findAll();

    ThirdParty newThirdParty(ThirdPartyDTO thirdPartyDTO);

    ThirdParty updateThirdParty(ThirdPartyDTO thirdPartyDTO);

    Address newAddress(AddressDTO addressDTO);

    LoginDetails newLoginDetails(LoginDetailsDTO loginDetailsDTO);

    AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO);
}
