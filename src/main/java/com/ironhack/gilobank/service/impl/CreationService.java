package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.*;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.AccountType;
import com.ironhack.gilobank.service.interfaces.IAccountHolderService;
import com.ironhack.gilobank.service.interfaces.IAddressService;
import com.ironhack.gilobank.service.interfaces.ICheckingAccountService;
import com.ironhack.gilobank.service.interfaces.ILoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreationService {

    @Autowired
    private IAddressService addressService;
    @Autowired
    private IAccountHolderService accountHolderService;
    @Autowired
    private ILoginDetailsService loginDetailsService;
    @Autowired
    private ICheckingAccountService checkingAccountService;

    public Address newAddress(AddressDTO addressDTO){
        Address address = new Address();
        if(!addressDTO.getId().toString().isEmpty()){
            address.setId(addressService.findById(addressDTO.getId()).getId());
        }
        if(!addressDTO.getHouseNumber().isEmpty()){
            address.setHouseNumber(addressDTO.getHouseNumber());
        }
        if(!addressDTO.getFlatNumber().isEmpty()){
            address.setFlatNumber(addressDTO.getFlatNumber());
        }
        if(!addressDTO.getStreet().isEmpty()){
            address.setStreet(addressDTO.getStreet());
        }
        if(!addressDTO.getTown().isEmpty()){
            address.setTown(addressDTO.getTown());
        }
        if(!addressDTO.getCity().isEmpty()){
            address.setCity(addressDTO.getCity());
        }
        if(!addressDTO.getPostcode().isEmpty()){
            address.setPostcode(addressDTO.getPostcode());
        }
        addressService.saveNewAddress(address);
        return address;
    }

    public AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO){
        AccountHolder accountHolder = new AccountHolder();
        if(!accountHolder.getId().toString().isEmpty()){
            accountHolder.setId(accountHolderDTO.getId());
        }
        if(!accountHolderDTO.getFirstName().isEmpty()){
            accountHolder.setFirstName(accountHolderDTO.getFirstName());
        }
        if(!accountHolderDTO.getSurname().isEmpty()){
            accountHolder.setSurname(accountHolderDTO.getSurname());
        }
        if(!accountHolderDTO.getDateOfBirth().toString().isEmpty()){
            accountHolder.setDateOfBirth(accountHolderDTO.getDateOfBirth());
        }
        if(accountHolderDTO.getPrimaryAddress() != null){
            accountHolder.setPrimaryAddress(accountHolderDTO.getPrimaryAddress());
        }
        if(accountHolderDTO.getMailingAddress() != null){
            accountHolder.setMailingAddress(accountHolderDTO.getMailingAddress());
        }
        if(accountHolderDTO.getRole() != null){
            accountHolder.setRole(accountHolderDTO.getRole());
        }
        accountHolderService.saveNewAccountHolder(accountHolder);
        return accountHolder;
    }

    public LoginDetails newLoginDetails(LoginDetailsDTO loginDetailsDTO){
        LoginDetails loginDetails = new LoginDetails();
        if(loginDetailsDTO.getId() != null){
            loginDetails.setId(loginDetailsService.findById(loginDetailsDTO.getId()).getId());
        }
        if(!loginDetailsDTO.getUsername().isEmpty()){
            loginDetails.setUsername(loginDetailsDTO.getUsername());
        }
        if(!loginDetailsDTO.getPassword().isEmpty()){
            loginDetails.setPassword(loginDetailsDTO.getPassword());
        }
        if(loginDetailsDTO.getUser() != null){
            loginDetails.setUser(loginDetailsDTO.getUser());
        }
        loginDetailsService.saveNewLoginDetails(loginDetails);
        return loginDetails;
    }

    public CheckingAccount newCheckingAccount(CheckingAccountDTO checkingAccountDTO){
        CheckingAccount checkingAccount = new CheckingAccount();
        if(checkingAccountDTO.getAccountNumber() != null){
            checkingAccount.setAccountNumber(
                    checkingAccountService.findByAccountNumber(
                            checkingAccountDTO.getAccountNumber()).getAccountNumber());
        }
        if(!checkingAccountDTO.getSecretKey().isEmpty()){
            checkingAccount.setSecretKey(checkingAccountDTO.getSecretKey());
        }
        if(checkingAccountDTO.getPrimaryHolder() != null){
            checkingAccount.setPrimaryHolder(checkingAccountDTO.getPrimaryHolder());
        }
        if(checkingAccountDTO.getSecondaryHolder() != null){
            checkingAccount.setSecondaryHolder(checkingAccountDTO.getSecondaryHolder());
        }
        if(checkingAccountDTO.getBalance() != null){
            checkingAccount.setBalance(checkingAccountDTO.getBalance());
        }
        if(checkingAccountDTO.getPenaltyFee() != null){
            checkingAccount.setPenaltyFee(checkingAccountDTO.getPenaltyFee());
        }
        if(checkingAccountDTO.getOpenDate() != null){
            checkingAccount.setOpenDate(checkingAccountDTO.getOpenDate());
        }
        if(checkingAccountDTO.getStatus() != null){
            checkingAccount.setStatus(checkingAccountDTO.getStatus());
        }
        if(checkingAccountDTO.getMonthlyMaintenanceFee() != null){
            checkingAccount.setMonthlyMaintenanceFee(checkingAccountDTO.getMonthlyMaintenanceFee());
        }
        if(checkingAccountDTO.getMinimumBalance() != null){
            checkingAccount.setMinimumBalance(checkingAccountDTO.getMinimumBalance());
        }
        checkingAccountService.saveCheckingAccount(checkingAccount);
        return checkingAccount;
    }

    public Account createAccount(AccountDTO accountDTO){
        if(accountDTO.getAccountType() == AccountType.CHECKING_ACCOUNT){
            CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
            checkingAccountDTO.setSecretKey(accountDTO.getSecretKey());
            checkingAccountDTO.setPrimaryHolder(accountDTO.getPrimaryHolder());
            checkingAccountDTO.setSecondaryHolder(accountDTO.getSecondaryHolder());
            return newCheckingAccount(checkingAccountDTO);
        }
        return null;
    }
}
