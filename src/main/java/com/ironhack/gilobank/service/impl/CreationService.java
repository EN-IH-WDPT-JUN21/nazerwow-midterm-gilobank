package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.*;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    private ICreditCardService creditCardService;
    @Autowired
    private ISavingsAccountService savingsAccountService;

    public Address newAddress(AddressDTO addressDTO) throws TransactionSystemException {
        Address address = new Address();
        if (addressDTO.getId() != null) {
            address = addressService.findById(addressDTO.getId());
        }
        if (addressDTO.getHouseNumber() != null) {
            address.setHouseNumber(addressDTO.getHouseNumber());
        }
        if (addressDTO.getFlatNumber() != null) {
            address.setFlatNumber(addressDTO.getFlatNumber());
        }
        if (addressDTO.getStreet() != null) {
            address.setStreet(addressDTO.getStreet());
        }
        if (addressDTO.getTown() != null) {
            address.setTown(addressDTO.getTown());
        }
        if (addressDTO.getCity() != null) {
            address.setCity(addressDTO.getCity());
        }
        if (addressDTO.getPostcode() != null) {
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
        if (accountHolderDTO.getFirstName() != null) {
            accountHolder.setFirstName(accountHolderDTO.getFirstName());
        }
        if (accountHolderDTO.getSurname() != null) {
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
        if (loginDetailsDTO.getUsername() != null) {
            loginDetails.setUsername(loginDetailsDTO.getUsername());
        }
        if (loginDetailsDTO.getPassword() != null) {
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
        if (checkingAccountDTO.getPrimaryHolder() != null) {
            if (!checkIfOver24(checkingAccountDTO.getPrimaryHolder()) &&
                    !checkIfOver24(checkingAccountDTO.getSecondaryHolder()) ||
                    !checkIfOver24(checkingAccountDTO.getPrimaryHolder()) && checkingAccountDTO.getSecondaryHolder() == null) {
                newStudentAccount(checkingAccountDTO);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Student Account Opened Instead");
            } else if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) != checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot open / edit due to 1 holder being a Student and One Holder Non-Student");
            }
        }
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
        if (checkingAccountDTO.getSecretKey() != null) {
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
        checkingAccountService.saveCheckingAccount(checkingAccount);
        return checkingAccount;
    }

    public StudentAccount newStudentAccount(CheckingAccountDTO checkingAccountDTO) throws TransactionSystemException {
        StudentAccount studentAccount = new StudentAccount();
        if (checkingAccountDTO.getPrimaryHolder() != null) {
            if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) &&
                    checkIfOver24(checkingAccountDTO.getSecondaryHolder()) ||
                    checkIfOver24(checkingAccountDTO.getPrimaryHolder()) && checkingAccountDTO.getSecondaryHolder() == null) {
                newCheckingAccount(checkingAccountDTO);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Student Account Opened Instead");
            } else if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) != checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot open / edit due to 1 holder being a Student and One Holder Non-Student");
            }
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
            if (checkingAccountDTO.getSecretKey() != null) {
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

    public CreditCard newCreditCard(CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard();
        if (creditCardDTO.getAccountNumber() != null) {
            creditCard = creditCardService.findByAccountNumberOptional(
                    creditCardDTO.getAccountNumber()).get();
        }
        if (creditCardDTO.getPrimaryHolder() != null) {
            creditCard.setPrimaryHolder(creditCardDTO.getPrimaryHolder());
        }
        if (creditCardDTO.getSecondaryHolder() != null) {
            creditCard.setSecondaryHolder(creditCardDTO.getSecondaryHolder());
        }
        if (creditCardDTO.getSecretKey() != null) {
            creditCard.setSecretKey(creditCardDTO.getSecretKey());
        }
        if (creditCardDTO.getBalance() != null) {
            creditCard.setBalance(creditCardDTO.getBalance());
        }
        if (creditCardDTO.getPenaltyFee() != null) {
            creditCard.setPenaltyFee(creditCardDTO.getPenaltyFee());
        }
        if (creditCardDTO.getOpenDate() != null) {
            creditCard.setOpenDate(creditCardDTO.getOpenDate());
        }
        if (creditCardDTO.getStatus() != null) {
            creditCard.setStatus(creditCardDTO.getStatus());
        }
        if (creditCardDTO.getCreditLimit() != null) {
            creditCard.setCreditLimit(creditCardDTO.getCreditLimit());
        }
        if (creditCardDTO.getInterestRate() != null) {
            creditCard.setInterestRate(creditCardDTO.getInterestRate());
        }
        creditCardService.saveNewCreditCard(creditCard);
        return creditCard;
    }

    public SavingsAccount newSavingsAccount(SavingsAccountDTO savingsAccountDTO) {
        SavingsAccount savingsAccount = new SavingsAccount();
        if (savingsAccountDTO.getAccountNumber() != null) {
            savingsAccount = savingsAccountService.findByAccountNumberOptional(
                    savingsAccountDTO.getAccountNumber()).get();
        }
        if (savingsAccountDTO.getPrimaryHolder() != null) {
            savingsAccount.setPrimaryHolder(savingsAccountDTO.getPrimaryHolder());
        }
        if (savingsAccountDTO.getSecondaryHolder() != null) {
            savingsAccount.setSecondaryHolder(savingsAccountDTO.getSecondaryHolder());
        }
        if (savingsAccountDTO.getSecretKey() != null) {
            savingsAccount.setSecretKey(savingsAccountDTO.getSecretKey());
        }
        if (savingsAccountDTO.getBalance() != null) {
            savingsAccount.setBalance(savingsAccountDTO.getBalance());
        }
        if (savingsAccountDTO.getPenaltyFee() != null) {
            savingsAccount.setPenaltyFee(savingsAccountDTO.getPenaltyFee());
        }
        if (savingsAccountDTO.getOpenDate() != null) {
            savingsAccount.setOpenDate(savingsAccountDTO.getOpenDate());
        }
        if (savingsAccountDTO.getStatus() != null) {
            savingsAccount.setStatus(savingsAccountDTO.getStatus());
        }
        if (savingsAccountDTO.getMinimumBalance() != null) {
            savingsAccount.setMinimumBalance(savingsAccountDTO.getMinimumBalance());
        }
        if (savingsAccountDTO.getInterestRate() != null) {
            savingsAccount.setInterestRate(savingsAccountDTO.getInterestRate());
        }
        savingsAccountService.saveNewSavingsAccount(savingsAccount);
        return savingsAccount;
    }


    public boolean checkIfOver24(AccountHolder accountHolder) {
        LocalDate dateOfBirth = accountHolder.getDateOfBirth();
        if (dateOfBirth == null) return true;
        LocalDate todaysDate = LocalDate.now();
        return !dateOfBirth.isAfter(todaysDate.minusYears(24));
    }
}
