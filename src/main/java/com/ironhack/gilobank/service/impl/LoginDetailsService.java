package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.repositories.LoginDetailsRepository;
import com.ironhack.gilobank.service.interfaces.ILoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LoginDetailsService implements ILoginDetailsService {

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;


    public LoginDetails findById(Long id) {
        Optional<LoginDetails> loginDetails = loginDetailsRepository.findById(id);
        if (loginDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Login Details exist with ID: " + id);
        }
        return loginDetails.get();
    }


    public void saveNewLoginDetails(LoginDetails loginDetails) {
        loginDetailsRepository.save(loginDetails);
    }
}
