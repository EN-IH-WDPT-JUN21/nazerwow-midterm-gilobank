package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.dao.*;

import java.util.List;

public interface IAdminController {

    List<Admin> getAll();
    ThirdParty newThirdParty(ThirdPartyDTO thirdPartyDTO);
    ThirdParty updateThirdParty(ThirdPartyDTO thirdPartyDTO);

    Address newAddress(AddressDTO addressDTO);
    Address updateAddress(AddressDTO addressDTO);

    AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO);
    AccountHolder updateAccountHolder(AccountHolderDTO accountHolderDTO);

    LoginDetails newLoginDetials(LoginDetailsDTO loginDetailsDTO);
    LoginDetails updateLoginDetails(LoginDetailsDTO loginDetailsDTO);
}
