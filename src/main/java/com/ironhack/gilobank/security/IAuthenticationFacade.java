package com.ironhack.gilobank.security;

import com.ironhack.gilobank.enums.Role;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    Object getPrincipal();

    Long getPrincipalId();

    Role getPrincipalRole();

    String getHashedKey();
}
