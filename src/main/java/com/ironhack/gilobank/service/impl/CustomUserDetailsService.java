package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.repositories.LoginDetailsRepository;
import com.ironhack.gilobank.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LoginDetails> loginDetails = loginDetailsRepository.findByUsername(username);

        if (!loginDetails.isPresent()) {
            throw new UsernameNotFoundException("User does not exists");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(loginDetails.get());

        return customUserDetails;
    }
}
