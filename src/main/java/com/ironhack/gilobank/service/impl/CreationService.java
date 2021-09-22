package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.*;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.AccountType;
import com.ironhack.gilobank.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

@Service
public class CreationService implements ICreationService {

    @Autowired
    private IAddressService addressService;
    @Autowired
    private IAccountHolderService accountHolderService;
    @Autowired
    private ILoginDetailsService loginDetailsService;
    @Autowired
    private ICheckingAccountService checkingAccountService;
    @Autowired
    private IStudentAccountService studentAccountService;

    public Address newAddress(AddressDTO addressDTO) throws TransactionSystemException {
        Address address = new Address();
        if (addressDTO.getId() != null) {
            address = addressService.findById(addressDTO.getId());
        }
        if (!addressDTO.getHouseNumber().isEmpty()) {
            address.setHouseNumber(addressDTO.getHouseNumber());
        }
        if (!addressDTO.getFlatNumber().isEmpty()) {
            address.setFlatNumber(addressDTO.getFlatNumber());
        }
        if (!addressDTO.getStreet().isEmpty()) {
            address.setStreet(addressDTO.getStreet());
        }
        if (!addressDTO.getTown().isEmpty()) {
            address.setTown(addressDTO.getTown());
        }
        if (!addressDTO.getCity().isEmpty()) {
            address.setCity(addressDTO.getCity());
        }
        if (!addressDTO.getPostcode().isEmpty()) {
            address.setPostcode(addressDTO.getPostcode());
        }
        addressService.saveNewAddress(address);
        return address;
    }

    public AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO) throws TransactionSystemException {
        AccountHolder accountHolder = new AccountHolder();
        if (accountHolderDTO.getId() != null) {
            accountHolder = accountHolderService.findById(accountHolderDTO.getId());
        }
        if (!accountHolderDTO.getFirstName().isEmpty()) {
            accountHolder.setFirstName(accountHolderDTO.getFirstName());
        }
        if (!accountHolderDTO.getSurname().isEmpty()) {
            accountHolder.setSurname(accountHolderDTO.getSurname());
        }
        if (accountHolderDTO.getDateOfBirth() != null) {
            accountHolder.setDateOfBirth(accountHolderDTO.getDateOfBirth());
        }
        if (accountHolderDTO.getPrimaryAddress() != null) {
            accountHolder.setPrimaryAddress(accountHolderDTO.getPrimaryAddress());
        }
        if (accountHolderDTO.getMailingAddress() != null) {
            accountHolder.setMailingAddress(accountHolderDTO.getMailingAddress());
        }
        if (accountHolderDTO.getRole() != null) {
            accountHolder.setRole(accountHolderDTO.getRole());
        }
        accountHolderService.saveNewAccountHolder(accountHolder);
        return accountHolder;
    }

    public LoginDetails newLoginDetails(LoginDetailsDTO loginDetailsDTO) throws TransactionSystemException {
        LoginDetails loginDetails = new LoginDetails();
        if (loginDetailsDTO.getId() != null) {
            loginDetails = loginDetailsService.findById(loginDetailsDTO.getId());
        }
        if (!loginDetailsDTO.getUsername().isEmpty()) {
            loginDetails.setUsername(loginDetailsDTO.getUsername());
        }
        if (!loginDetailsDTO.getPassword().isEmpty()) {
            loginDetails.setPassword(loginDetailsDTO.getPassword());
        }
        if (loginDetailsDTO.getUser() != null) {
            loginDetails.setUser(loginDetailsDTO.getUser());
        }
        loginDetailsService.saveNewLoginDetails(loginDetails);
        return loginDetails;
    }

    public CheckingAccount newCheckingAccount(CheckingAccountDTO checkingAccountDTO) throws TransactionSystemException {
        CheckingAccount checkingAccount = new CheckingAccount();
        if (!checkIfOver24(checkingAccountDTO.getPrimaryHolder()) &&
                !checkIfOver24(checkingAccountDTO.getSecondaryHolder()) ||
                !checkIfOver24(checkingAccountDTO.getPrimaryHolder()) && checkingAccountDTO.getSecondaryHolder() == null) {
            newStudentAccount(checkingAccountDTO);
        } else if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) != checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot open a joint account due to age difference");
        } else {
            if (checkingAccountDTO.getAccountNumber() != null) {
                checkingAccount = checkingAccountService.findByAccountNumberOptional(
                        checkingAccountDTO.getAccountNumber()).get();
            }
            if (checkingAccountDTO.getPrimaryHolder() != null) {
                checkingAccount.setPrimaryHolder(checkingAccountDTO.getPrimaryHolder());
            }
            if (checkingAccountDTO.getSecondaryHolder() != null) {
                checkingAccount.setSecondaryHolder(checkingAccountDTO.getSecondaryHolder());
            }
            if (!checkingAccountDTO.getSecretKey().isEmpty()) {
                checkingAccount.setSecretKey(checkingAccountDTO.getSecretKey());
            }
            if (checkingAccountDTO.getBalance() != null) {
                checkingAccount.setBalance(checkingAccountDTO.getBalance());
            }
            if (checkingAccountDTO.getPenaltyFee() != null) {
                checkingAccount.setPenaltyFee(checkingAccountDTO.getPenaltyFee());
            }
            if (checkingAccountDTO.getOpenDate() != null) {
                checkingAccount.setOpenDate(checkingAccountDTO.getOpenDate());
            }
            if (checkingAccountDTO.getStatus() != null) {
                checkingAccount.setStatus(checkingAccountDTO.getStatus());
            }
            if (checkingAccountDTO.getMonthlyMaintenanceFee() != null) {
                checkingAccount.setMonthlyMaintenanceFee(checkingAccountDTO.getMonthlyMaintenanceFee());
            }
            if (checkingAccountDTO.getMinimumBalance() != null) {
                checkingAccount.setMinimumBalance(checkingAccountDTO.getMinimumBalance());
            }
        }
        checkingAccountService.saveCheckingAccount(checkingAccount);
        return checkingAccount;
    }

    public StudentAccount newStudentAccount(CheckingAccountDTO checkingAccountDTO) throws TransactionSystemException {
        StudentAccount studentAccount = new StudentAccount();
        if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) &&
                checkIfOver24(checkingAccountDTO.getSecondaryHolder()) ||
                checkIfOver24(checkingAccountDTO.getPrimaryHolder()) && checkingAccountDTO.getSecondaryHolder() == null) {
            newCheckingAccount(checkingAccountDTO);
        } else if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) != checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot open a joint account due to age difference");
        } else {
            if (checkingAccountDTO.getAccountNumber() != null) {
                studentAccount = studentAccountService.findByAccountNumberOptional(
                        checkingAccountDTO.getAccountNumber()).get();
            }
            if (checkingAccountDTO.getPrimaryHolder() != null) {
                studentAccount.setPrimaryHolder(checkingAccountDTO.getPrimaryHolder());
            }
            if (checkingAccountDTO.getSecondaryHolder() != null) {
                studentAccount.setSecondaryHolder(checkingAccountDTO.getSecondaryHolder());
            }
            if (!checkingAccountDTO.getSecretKey().isEmpty()) {
                studentAccount.setSecretKey(checkingAccountDTO.getSecretKey());
            }
            if (checkingAccountDTO.getBalance() != null) {
                studentAccount.setBalance(checkingAccountDTO.getBalance());
            }
            if (checkingAccountDTO.getPenaltyFee() != null) {
                studentAccount.setPenaltyFee(checkingAccountDTO.getPenaltyFee());
            }
            if (checkingAccountDTO.getOpenDate() != null) {
                studentAccount.setOpenDate(checkingAccountDTO.getOpenDate());
            }
            if (checkingAccountDTO.getStatus() != null) {
                studentAccount.setStatus(checkingAccountDTO.getStatus());
            }
        }
        studentAccountService.saveNewStudentAccount(studentAccount);
        return studentAccount;
    }

//    public Account createAccount(AccountDTO accountDTO) throws ConstraintViolationException {
//        if (accountDTO.getAccountType() == AccountType.CHECKING_ACCOUNT) {
//            CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO();
//            checkingAccountDTO.setSecretKey(accountDTO.getSecretKey());
//            checkingAccountDTO.setPrimaryHolder(accountDTO.getPrimaryHolder());
//            checkingAccountDTO.setSecondaryHolder(accountDTO.getSecondaryHolder());
//            return newCheckingAccount(checkingAccountDTO);
//        }
//        return null;
//    }

    public boolean checkIfOver24(AccountHolder accountHolder) {
        LocalDate dateOfBirth = accountHolder.getDateOfBirth();
        if (dateOfBirth == null) return true;
        LocalDate todaysDate = LocalDate.now();
        return !dateOfBirth.isAfter(todaysDate.minusYears(24));
    }
}
