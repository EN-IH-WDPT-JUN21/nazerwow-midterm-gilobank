package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.dao.Admin;
import com.ironhack.gilobank.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAll() {
        return adminService.findAll();
    }
}
