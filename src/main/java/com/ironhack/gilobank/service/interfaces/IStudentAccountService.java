package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.StudentAccount;

import java.util.List;
import java.util.Optional;

public interface IStudentAccountService {

    List<StudentAccount> findAll();
    Optional<StudentAccount> findByAccountNumber(Long accountNumber);
}
