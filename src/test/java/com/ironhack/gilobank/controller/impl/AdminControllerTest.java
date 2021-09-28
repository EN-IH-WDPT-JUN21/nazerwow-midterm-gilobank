package com.ironhack.gilobank.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.gilobank.controller.dto.AccountHolderDTO;
import com.ironhack.gilobank.controller.dto.AddressDTO;
import com.ironhack.gilobank.controller.dto.LoginDetailsDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Status;
import com.ironhack.gilobank.repositories.*;
import com.ironhack.gilobank.security.CustomUserDetails;
import com.ironhack.gilobank.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private LoginDetailsRepository loginDetailsRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Address testAddress1;
    private Address testAddress2;
    private AccountHolder testHolder1;
    private AccountHolder testHolder2;
    private CheckingAccount testAccount1, testAccount2, testAccount3;
    private LoginDetails loginDetails1, loginDetails2, loginDetails3, loginDetails4, loginDetails5;
    private Admin admin, admin2, admin3;
    private ThirdParty thirdParty, thirdParty2;
    private CustomUserDetails details1, details2, details3, details4, details5;
    private UsernamePasswordAuthenticationToken adminLogin, login1, login2, thirdPartyLogin, thirdPartyLogin2;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate testDateOfBirth1 = LocalDate.parse("1988-01-01");
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate testDateOfBirth2 = LocalDate.parse("1994-01-01");
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate dateNow = LocalDate.now();


    @BeforeEach
    void setUp() throws ParseException {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testAddress1 = new Address("1", "Primary Road", "Primary", "PRIMA1");
        testAddress2 = new Address("2", "Mailing Road", "Mailing", "MAILI1");

        testHolder1 = new AccountHolder("Test1", "TestSur1", testDateOfBirth1, testAddress1, null);
        testHolder2 = new AccountHolder("Test2", "TestSur2", testDateOfBirth2, testAddress2, null);
        admin = new Admin("Admin");
        admin2 = new Admin("Admin2");
        admin3 = new Admin("Admin3");
        thirdParty = new ThirdParty("ThirdParty", "hashedKey");
        thirdParty2 = new ThirdParty("ThirdParty2", "haskedKey2");

        loginDetails1 = new LoginDetails("hackerman", "ihackthings", testHolder1);
        loginDetails2 = new LoginDetails("testusername2", "testpass2", testHolder2);
        loginDetails3 = new LoginDetails("testAdmin", "testpass", admin);
        loginDetails4 = new LoginDetails("testThirdParty", "testpass", thirdParty);
        loginDetails5 = new LoginDetails("TestThirdParty2", "testpass2", thirdParty2);

        details1 = new CustomUserDetails(loginDetails1);
        details2 = new CustomUserDetails(loginDetails2);
        details3 = new CustomUserDetails(loginDetails3);
        details4 = new CustomUserDetails(loginDetails4);
        details5 = new CustomUserDetails(loginDetails5);

        testAccount1 = new CheckingAccount(
                "secretKey1",
                testHolder1,                    // Primary Holder
                testHolder2,                    // Secondary Holder
                new Money(new BigDecimal("1000")),     // balance
                new Money(new BigDecimal("10")),       // penaltyFee
                LocalDate.parse("2011-01-01"),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("11")),      // monthly maintenance Balance
                new Money(new BigDecimal("100")));      // minimum balance
        testAccount2 = new CheckingAccount(
                "secretKey2",
                testHolder1,                    // Primary Holder
                null,
                new Money(new BigDecimal("2000")),     // balance
                new Money(new BigDecimal("20")),       // penaltyFee
                LocalDate.parse("2012-02-02"),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("22")),      // monthly maintenance
                new Money(new BigDecimal("200")));      // minimum balance
        testAccount3 = new CheckingAccount(
                "secretKey3",
                testHolder2,                    // Primary Holder
                null,
                new Money(new BigDecimal("3000")),     // balance
                new Money(new BigDecimal("30")),       // penaltyFee
                LocalDate.parse("2013-03-03"),  // open date
                Status.ACTIVE,                  // Status
                new Money(new BigDecimal("300")),      // monthly maintenance
                new Money(new BigDecimal("300")));      // minimum balance

        addressRepository.saveAll(List.of(testAddress1, testAddress2));
        accountHolderRepository.saveAll(List.of(testHolder1, testHolder2));
        adminRepository.saveAll(List.of(admin, admin2, admin3));
        thirdPartyRepository.save(thirdParty);
        loginDetailsRepository.saveAll(List.of(loginDetails1, loginDetails2, loginDetails4));
        checkingAccountRepository.saveAll(List.of(testAccount1, testAccount2, testAccount3));

        login1 = new UsernamePasswordAuthenticationToken(details1, "x");
        login2 = new UsernamePasswordAuthenticationToken(details2, "x");
        adminLogin = new UsernamePasswordAuthenticationToken(details3, "z",
                singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        thirdPartyLogin = new UsernamePasswordAuthenticationToken(details4, "z",
                singleton(new SimpleGrantedAuthority("ROLE_THIRDPARTY")));
        thirdPartyLogin2 = new UsernamePasswordAuthenticationToken(details5, "z",
                singleton(new SimpleGrantedAuthority("ROLE_THIRDPARTY")));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        loginDetailsRepository.deleteAll();
        adminRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void getAdminById() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/api/admin/" + admin2.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Admin2"));
        assertFalse(result.getResponse().getContentAsString().contains("Admin3"));
    }

    @Test
    void getAll_ValidTest() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        MvcResult result = mockMvc.perform(
                        get("/api/admin"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Admin"));
        assertTrue(result.getResponse().getContentAsString().contains("Admin2"));
        assertTrue(result.getResponse().getContentAsString().contains("Admin3"));
    }


    @Test
    void createThirdParty() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("name", "newhash2344");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/thirdparty/new")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("newhash2344"));
    }

    @Test
    void updateThirdParty() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("newName", "newhash2344");
        thirdPartyDTO.setId(thirdParty.getId());
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/thirdparty/update")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("newhash2344"));
        assertTrue(thirdPartyRepository.findByHashedKey("newHash2344").isPresent());
    }

    @Test
    void newAddress() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setHouseNumber("25");
        addressDTO.setStreet("New Street");
        addressDTO.setCity("New City");
        addressDTO.setPostcode("TF11 111");
        String body = objectMapper.writeValueAsString(addressDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/address/new")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("New City"));
    }

    @Test
    void updateAddress() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(testAddress1.getId());
        addressDTO.setHouseNumber("25");
        addressDTO.setStreet("New Street");
        addressDTO.setCity("New City");
        addressDTO.setPostcode("TF11 111");
        String body = objectMapper.writeValueAsString(addressDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/address/update")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("New City"));
        assertEquals("New Street", addressRepository.findById(testAddress1.getId()).get().getStreet());
    }

    @Test
    void newAccountHolder() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setFirstName("NewFirstName");
        accountHolderDTO.setSurname("NewSurname");
        accountHolderDTO.setPrimaryAddress(testAddress1);
        accountHolderDTO.setDateOfBirth(LocalDate.parse("2000-01-01"));
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/accountholder/new")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("NewFirstName"));
    }

    @Test
    void updateAccountHolder() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setId(testHolder1.getId());
        accountHolderDTO.setFirstName("NewFirstName");
        accountHolderDTO.setSurname("NewSurname");
        accountHolderDTO.setPrimaryAddress(testAddress1);
        accountHolderDTO.setDateOfBirth(LocalDate.parse("2000-01-01"));
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/accountholder/update")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("NewFirstName"));
        assertEquals("NewFirstName", accountHolderRepository.findById(testHolder1.getId()).get().getFirstName());
    }

    @Test
    void newLoginDetails() throws Exception {
        AccountHolder newHolder = new AccountHolder("newHolder", "newsurname", testDateOfBirth2, testAddress2, null);

        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        LoginDetailsDTO loginDetailsDTO = new LoginDetailsDTO("newUsername", "newPassword", newHolder);
        String body = objectMapper.writeValueAsString(loginDetailsDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/logindetails/new")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("newUsername"));
    }

    @Test
    void updateLoginDetails() throws Exception {
        AccountHolder newHolder = new AccountHolder("newHolder", "newsurname", testDateOfBirth2, testAddress2, null);

        SecurityContextHolder.getContext().setAuthentication(adminLogin);
        LoginDetailsDTO loginDetailsDTO = new LoginDetailsDTO("newUsername", "newPassword", newHolder);
        loginDetailsDTO.setId(loginDetails1.getId());
        String body = objectMapper.writeValueAsString(loginDetailsDTO);
        MvcResult result = mockMvc.perform(
                        put("/api/admin/logindetails/update")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("newUsername"));
        assertEquals("newUsername", loginDetailsRepository.findById(loginDetails1.getId()).get().getUsername());
    }
}