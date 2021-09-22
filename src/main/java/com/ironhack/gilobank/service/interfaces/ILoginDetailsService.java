package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.LoginDetails;

public interface ILoginDetailsService {

    LoginDetails findById(Long id);

    void saveNewLoginDetails(LoginDetails loginDetails);
}
