package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.dao.Admin;
import com.ironhack.gilobank.repositories.AdminRepository;
import com.ironhack.gilobank.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }
}
