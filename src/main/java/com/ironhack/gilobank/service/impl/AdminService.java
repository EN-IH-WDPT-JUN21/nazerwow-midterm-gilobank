package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.repositories.AdminRepository;
import com.ironhack.gilobank.service.interfaces.IAdminService;
import com.ironhack.gilobank.service.interfaces.ICreationService;
import com.ironhack.gilobank.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ICreationService creationService;
    @Autowired
    private IThirdPartyService thirdPartyService;

    public Admin findById(Long id){
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Admin found with id: " + id);
        return admin.get();
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public ThirdParty newThirdParty(ThirdPartyDTO thirdPartyDTO) {
        return creationService.newThirdParty(thirdPartyDTO);
    }

    public ThirdParty updateThirdParty(ThirdPartyDTO thirdPartyDTO) {
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
