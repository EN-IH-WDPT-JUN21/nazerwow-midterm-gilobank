package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.repositories.AdminRepository;
import com.ironhack.gilobank.service.interfaces.IAdminService;
import com.ironhack.gilobank.service.interfaces.ICreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ICreationService creationService;

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public ThirdParty newThirdParty(ThirdPartyDTO thirdPartyDTO) {
        return creationService.newThirdParty(thirdPartyDTO);
    }

    public Address newAddress(AddressDTO addressDTO) {
        return creationService.newAddress(addressDTO);
    }

    public LoginDetails newLoginDetails(LoginDetailsDTO loginDetailsDTO) {
        return creationService.newLoginDetails(loginDetailsDTO);
    }

    public AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO) {
        return creationService.newAccountHolder(accountHolderDTO);
    }
}
