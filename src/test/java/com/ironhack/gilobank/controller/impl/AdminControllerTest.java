package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.dao.Admin;
import com.ironhack.gilobank.dao.LoginDetails;
import com.ironhack.gilobank.repositories.AdminRepository;
import com.ironhack.gilobank.repositories.LoginDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private Admin testAdmin1;
    private Admin testAdmin2;
    private LoginDetails loginDetails1;
    private LoginDetails loginDetails2;


    @BeforeEach
    void setUp() throws ParseException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        loginDetails1 = new LoginDetails("hackerman", "ihackthings");
        loginDetails2 = new LoginDetails("testusername2", "testpass2");

        testAdmin1 = new Admin(loginDetails1, "Test1");
        testAdmin2 = new Admin(loginDetails2, "Test2");

        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2));
        adminRepository.saveAll(List.of(testAdmin1, testAdmin2));
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
        loginDetailsRepository.deleteAll();
    }

    @Test
    void getAll_ValidTest() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/user/admin"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Test1"));
        assertTrue(result.getResponse().getContentAsString().contains("Test2"));
    }
}