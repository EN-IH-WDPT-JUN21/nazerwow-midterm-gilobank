package com.ironhack.gilobank.security;

import com.ironhack.gilobank.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                // Checking
                .mvcMatchers(HttpMethod.GET, "/api/account/checking/{id}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/checking/{id}/{dateFrom}/{dateTo}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/checking/credit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/checking/debit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/checking/transfer").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/checking/{id}/balance").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                // CreditCard
                .mvcMatchers(HttpMethod.GET, "/api/account/creditcard/{id}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/creditcard/{id}/{dateFrom}/{dateTo}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/creditcard/credit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/creditcard/debit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/creditcard/transfer").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/creditcard/{id}/balance").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                // Savings Accounts
                .mvcMatchers(HttpMethod.GET, "/api/account/saving/{id}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/saving/{id}/{dateFrom}/{dateTo}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/saving/credit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/saving/debit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/saving/transfer").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/saving/{id}/balance").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                // Student Accounts
                .mvcMatchers(HttpMethod.GET, "/api/account/student/{id}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/student/{id}/{dateFrom}/{dateTo}").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/student/credit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/student/debit").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/account/student/transfer").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/account/student/{id}/balance").hasAnyRole("ACCOUNTHOLDER", "ADMIN")
                // Third Party
                .mvcMatchers(HttpMethod.PUT, "/api/thirdparty/{hashedkey}/credit").hasAnyRole("THIRDPARTY", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/thirdparty/{hashedkey}/debit").hasAnyRole("THIRDPARTY", "ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/thirdparty/{hashedkey}/transfer").hasAnyRole("THIRDPARTY", "ADMIN")
                .anyRequest().permitAll();

    }

}
