package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAll() {
        return adminService.findAll();
    }

    @PutMapping("/thirdparty/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ThirdParty createThirdParty(@RequestBody ThirdPartyDTO thirdPartyDTO) {
        return adminService.newThirdParty(thirdPartyDTO);
    }

    @PutMapping("/thirdparty/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ThirdParty updateThirdParty(@RequestBody ThirdPartyDTO thirdPartyDTO) {
        return adminService.updateThirdParty(thirdPartyDTO);
    }

    @PutMapping("/address/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Address newAddress(@RequestBody AddressDTO addressDTO) {
        return adminService.newAddress(addressDTO);
    }

    @PutMapping("/address/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Address updateAddress(@RequestBody AddressDTO addressDTO) {
        return adminService.newAddress(addressDTO);
    }

    @PutMapping("/accountholder/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountHolder newAccountHolder(@RequestBody AccountHolderDTO accountHolderDTO) {
        return adminService.newAccountHolder(accountHolderDTO);
    }

    @PutMapping("/accountholder/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountHolder updateAccountHolder(@RequestBody AccountHolderDTO accountHolderDTO) {
        return adminService.newAccountHolder(accountHolderDTO);
    }

    @PutMapping("/logindetails/new")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginDetails newLoginDetails(@RequestBody LoginDetailsDTO loginDetailsDTO) {
        return adminService.newLoginDetails(loginDetailsDTO);
    }

    @PutMapping("/logindetails/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public LoginDetails updateLoginDetails(@RequestBody LoginDetailsDTO loginDetailsDTO) {
        return adminService.newLoginDetails(loginDetailsDTO);
    }
}
