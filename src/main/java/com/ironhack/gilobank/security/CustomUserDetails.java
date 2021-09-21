package com.ironhack.gilobank.security;

import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@NoArgsConstructor
@Setter
@Getter
public class CustomUserDetails implements UserDetails {

    private LoginDetails loginDetails;

    public CustomUserDetails(LoginDetails loginDetails) {
        this.loginDetails = loginDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + loginDetails.getUser().getRole()));
        return authorities;
    }


    @Override
    public String getPassword() {
        return loginDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return loginDetails.getUsername();
    }

    public Long getId() {
        return loginDetails.getId();
    }

    public Role getRole() {
        return loginDetails.getUser().getRole();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
