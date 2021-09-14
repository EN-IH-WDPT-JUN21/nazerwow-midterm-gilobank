package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.StudentAccount;

import java.util.List;
import java.util.Optional;

public interface IStudentAccountController {

    List<StudentAccount> getAll();
    Optional<StudentAccount> getByAccountNumber(Long accountNumber);
}
