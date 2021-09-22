package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.dao.StudentAccount;

import java.util.List;
import java.util.Optional;

public interface IStudentAccountService {

    List<StudentAccount> findAll();

    StudentAccount findByAccountNumber(Long accountNumber);
    Optional<StudentAccount> findByAccountNumberOptional(Long accountNumber);
    void saveNewStudentAccount(StudentAccount studentAccount);
}
