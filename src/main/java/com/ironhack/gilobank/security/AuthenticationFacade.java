package com.ironhack.gilobank.security;

import com.ironhack.gilobank.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getPrincipalId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        return null;
    }

    public Role getPrincipalRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getRole();
        }
        return null;
    }

    public String getHashedKey() {
        Object principal = getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getHashedKey();
        }
        return null;
    }
}
