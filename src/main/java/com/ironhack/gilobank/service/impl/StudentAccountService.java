package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.StudentAccount;
import com.ironhack.gilobank.repositories.StudentAccountRepository;
import com.ironhack.gilobank.service.interfaces.IStudentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentAccountService implements IStudentAccountService {

    @Autowired
    private StudentAccountRepository studentAccountRepository;

    public List<StudentAccount> findAll() {
        return studentAccountRepository.findAllSecure();
    }

    @Override
    public Optional<StudentAccount> findByAccountNumber(Long accountNumber) {
        Optional<StudentAccount> optionalStudentAccount = studentAccountRepository.findByAccountNumberSecure(accountNumber);
        if (optionalStudentAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Student Account Found with Account Number :" + accountNumber);

        return optionalStudentAccount;

    }


}
