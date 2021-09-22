package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.dao.StudentAccount;

import java.util.List;

public interface IStudentAccountController {

    List<StudentAccount> getAll();

    StudentAccount getByAccountNumber(Long accountNumber);
}
