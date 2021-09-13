package com.ironhack.gilobank.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface ICustomUserDetailsService {

    UserDetails loadUserByUsername(String username);

}
