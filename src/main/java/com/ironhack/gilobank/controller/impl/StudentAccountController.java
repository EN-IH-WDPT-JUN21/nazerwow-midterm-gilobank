package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.interfaces.IStudentAccountController;
import com.ironhack.gilobank.dao.StudentAccount;
import com.ironhack.gilobank.service.interfaces.IStudentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account/student")
public class StudentAccountController implements IStudentAccountController {

    @Autowired
    private IStudentAccountService studentAccountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentAccount> getAll() {
        return studentAccountService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<StudentAccount> getByAccountNumber(@PathVariable(name = "id") Long accountNumber) {
        return studentAccountService.findByAccountNumber(accountNumber);
    }

}
