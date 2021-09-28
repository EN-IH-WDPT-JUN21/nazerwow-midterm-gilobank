package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.*;
import com.ironhack.gilobank.dao.*;
import com.ironhack.gilobank.enums.Role;
import com.ironhack.gilobank.service.interfaces.*;
import com.ironhack.gilobank.utils.Money;
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
    @Autowired
    private IThirdPartyService thirdPartyService;
    @Autowired
    private IAdminService adminService;

    // This will create a new address or update an existing one if found
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

    // This will create a new account holder or update an existing one if found
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
            accountHolder.setPrimaryAddress(findOrCreateAddress(accountHolderDTO.getPrimaryAddress()));
        }
        if (accountHolderDTO.getMailingAddress() != null) {
            accountHolder.setMailingAddress(findOrCreateAddress(accountHolderDTO.getMailingAddress()));
        }
        if (accountHolderDTO.getRole() != null) {
            accountHolder.setRole(accountHolderDTO.getRole());
        }
        if (accountHolderDTO.getLoginDetails() != null) {
            accountHolder.setLoginDetails(findOrCreateLoginDetails(accountHolderDTO.getLoginDetails()));
        }
        accountHolderService.saveNewAccountHolder(accountHolder);
        return accountHolder;
    }

    // This will create or update thirdparty
    public ThirdParty newThirdParty(ThirdPartyDTO thirdPartyDTO) {
        ThirdParty thirdParty = new ThirdParty();
        if (thirdPartyDTO.getId() != null) {
            thirdParty = thirdPartyService.findById(thirdPartyDTO.getId());
        }
        if (thirdPartyDTO.getHashedKey() != null) {
            thirdParty.setHashedKey(thirdPartyDTO.getHashedKey());
        }
        if (thirdPartyDTO.getName() != null) {
            thirdParty.setName(thirdPartyDTO.getName());
        }
        if (thirdPartyDTO.getRole() != null) {
            thirdParty.setRole(Role.THIRDPARTY);
        }
        thirdPartyService.saveThirdParty(thirdParty);
        return thirdParty;
    }

    // this will create or update login details
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

    // this will create or update checking account
    public Account newCheckingAccount(CheckingAccountDTO checkingAccountDTO) throws TransactionSystemException {
        CheckingAccount checkingAccount = new CheckingAccount();
        if (checkingAccountDTO.getAccountNumber() != null) {
            checkingAccount = checkingAccountService.findByAccountNumberOptional(
                    checkingAccountDTO.getAccountNumber()).get();
        }
        // This will check if holder is 24 or over
        if (checkingAccountDTO.getPrimaryHolder() != null) {
            checkingAccount.setPrimaryHolder(findOrCreateHolder(checkingAccountDTO.getPrimaryHolder()));
            if (!checkIfOver24(checkingAccount.getPrimaryHolder()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Primary Holder is Under 24");
        }
        // This will check if holder is 24 or over
        if (checkingAccountDTO.getSecondaryHolder() != null) {
            checkingAccount.setSecondaryHolder(findOrCreateHolder(checkingAccountDTO.getSecondaryHolder()));
            if (!checkIfOver24(checkingAccount.getSecondaryHolder()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Secondary Holder is Under 24");
        }
        if (checkingAccountDTO.getSecretKey() != null) {
            checkingAccount.setSecretKey(checkingAccountDTO.getSecretKey());
        }
        if (checkingAccountDTO.getBalance() != null) {
            Money amountAsMoney = new Money(checkingAccountDTO.getBalance());
            checkingAccount.setBalance(amountAsMoney);
        }
        if (checkingAccountDTO.getPenaltyFee() != null) {
            Money amountAsMoney = new Money(checkingAccountDTO.getPenaltyFee());
            checkingAccount.setPenaltyFee(amountAsMoney);
        }
        if (checkingAccountDTO.getOpenDate() != null) {
            checkingAccount.setOpenDate(checkingAccountDTO.getOpenDate());
        }
        if (checkingAccountDTO.getStatus() != null) {
            checkingAccount.setStatus(checkingAccountDTO.getStatus());
        }
        if (checkingAccountDTO.getMonthlyMaintenanceFee() != null) {
            Money amountAsMoney = new Money(checkingAccountDTO.getMonthlyMaintenanceFee());
            checkingAccount.setMonthlyMaintenanceFee(amountAsMoney);
        }
        if (checkingAccountDTO.getMinimumBalance() != null) {
            Money amountAsMoney = new Money(checkingAccountDTO.getMinimumBalance());
            checkingAccount.setMinimumBalance(amountAsMoney);
        }
        checkingAccountService.saveCheckingAccount(checkingAccount);
        return checkingAccount;
    }

    // This will create or update student account
    public Account newStudentAccount(CheckingAccountDTO checkingAccountDTO) throws TransactionSystemException {
        StudentAccount studentAccount = new StudentAccount();
        if (checkingAccountDTO.getAccountNumber() != null) {
            studentAccount = studentAccountService.findByAccountNumberOptional(
                    checkingAccountDTO.getAccountNumber()).get();
        }
        // This will check if holder is 24 or over
        if (checkingAccountDTO.getPrimaryHolder() != null) {
            studentAccount.setPrimaryHolder(findOrCreateHolder(checkingAccountDTO.getPrimaryHolder()));
            if (checkIfOver24(studentAccount.getPrimaryHolder()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Primary Holder is Over 24");
        }
        // This will check if holder is 24 or over
        if (checkingAccountDTO.getSecondaryHolder() != null) {
            studentAccount.setSecondaryHolder(findOrCreateHolder(checkingAccountDTO.getPrimaryHolder()));
            if (checkIfOver24(studentAccount.getSecondaryHolder()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Secondary Holder is Over 24");
        }
        if (checkingAccountDTO.getSecretKey() != null) {
            studentAccount.setSecretKey(checkingAccountDTO.getSecretKey());
        }
        if (checkingAccountDTO.getBalance() != null) {
            Money amountAsMoney = new Money(checkingAccountDTO.getBalance());
            studentAccount.setBalance(amountAsMoney);
        }
        if (checkingAccountDTO.getPenaltyFee() != null) {
            Money amountAsMoney = new Money(checkingAccountDTO.getPenaltyFee());
            studentAccount.setPenaltyFee(amountAsMoney);
        }
        if (checkingAccountDTO.getOpenDate() != null) {
            studentAccount.setOpenDate(checkingAccountDTO.getOpenDate());
        }
        if (checkingAccountDTO.getStatus() != null) {
            studentAccount.setStatus(checkingAccountDTO.getStatus());
        }
        studentAccountService.saveNewStudentAccount(studentAccount);
        return studentAccount;
    }
    // this will create or update credit card
    public CreditCard newCreditCard(CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard();
        if (creditCardDTO.getAccountNumber() != null) {
            creditCard = creditCardService.findByAccountNumberOptional(
                    creditCardDTO.getAccountNumber()).get();
        }
        if (creditCardDTO.getPrimaryHolder() != null) {
            creditCard.setPrimaryHolder(findOrCreateHolder(creditCardDTO.getPrimaryHolder()));
        }
        if (creditCardDTO.getSecondaryHolder() != null) {
            creditCard.setSecondaryHolder(findOrCreateHolder(creditCardDTO.getSecondaryHolder()));
        }
        if (creditCardDTO.getSecretKey() != null) {
            creditCard.setSecretKey(creditCardDTO.getSecretKey());
        }
        if (creditCardDTO.getBalance() != null) {
            Money amountAsMoney = new Money(creditCardDTO.getBalance());
            creditCard.setBalance(amountAsMoney);
        }
        if (creditCardDTO.getPenaltyFee() != null) {
            Money amountAsMoney = new Money(creditCardDTO.getPenaltyFee());
            creditCard.setPenaltyFee(amountAsMoney);
        }
        if (creditCardDTO.getOpenDate() != null) {
            creditCard.setOpenDate(creditCardDTO.getOpenDate());
        }
        if (creditCardDTO.getStatus() != null) {
            creditCard.setStatus(creditCardDTO.getStatus());
        }
        if (creditCardDTO.getCreditLimit() != null) {
            Money amountAsMoney = new Money(creditCardDTO.getCreditLimit());
            creditCard.setCreditLimit(amountAsMoney);
        }
        if (creditCardDTO.getInterestRate() != null) {
            creditCard.setInterestRate(creditCardDTO.getInterestRate());
        }
        creditCardService.saveNewCreditCard(creditCard);
        return creditCard;
    }
    // This will update or create a savings account
    public SavingsAccount newSavingsAccount(SavingsAccountDTO savingsAccountDTO) {
        SavingsAccount savingsAccount = new SavingsAccount();
        if (savingsAccountDTO.getAccountNumber() != null) {
            savingsAccount = savingsAccountService.findByAccountNumberOptional(
                    savingsAccountDTO.getAccountNumber()).get();
        }
        if (savingsAccountDTO.getPrimaryHolder() != null) {
            savingsAccount.setPrimaryHolder(findOrCreateHolder(savingsAccountDTO.getPrimaryHolder()));
        }
        if (savingsAccountDTO.getSecondaryHolder() != null) {
            savingsAccount.setSecondaryHolder(findOrCreateHolder(savingsAccountDTO.getSecondaryHolder()));
        }
        if (savingsAccountDTO.getSecretKey() != null) {
            savingsAccount.setSecretKey(savingsAccountDTO.getSecretKey());
        }
        if (savingsAccountDTO.getBalance() != null) {
            Money amountAsMoney = new Money(savingsAccountDTO.getBalance());
            savingsAccount.setBalance(amountAsMoney);
        }
        if (savingsAccountDTO.getPenaltyFee() != null) {
            Money amountAsMoney = new Money(savingsAccountDTO.getPenaltyFee());
            savingsAccount.setPenaltyFee(amountAsMoney);
        }
        if (savingsAccountDTO.getOpenDate() != null) {
            savingsAccount.setOpenDate(savingsAccountDTO.getOpenDate());
        }
        if (savingsAccountDTO.getStatus() != null) {
            savingsAccount.setStatus(savingsAccountDTO.getStatus());
        }
        if (savingsAccountDTO.getMinimumBalance() != null) {
            Money amountAsMoney = new Money(savingsAccountDTO.getMinimumBalance());
            savingsAccount.setMinimumBalance(amountAsMoney);
        }
        if (savingsAccountDTO.getInterestRate() != null) {
            savingsAccount.setInterestRate(savingsAccountDTO.getInterestRate());
        }
        savingsAccountService.saveNewSavingsAccount(savingsAccount);
        return savingsAccount;
    }

    // This return true if accountholder is over 24
    public boolean checkIfOver24(AccountHolder accountHolder) {
        LocalDate dateOfBirth = accountHolder.getDateOfBirth();
        if (dateOfBirth == null) return true;
        LocalDate todaysDate = LocalDate.now();
        return !dateOfBirth.isAfter(todaysDate.minusYears(24));
    }

    // This will update or create an account holder automatically during account opening
    public AccountHolder findOrCreateHolder(AccountHolder accountHolder) {
        if (accountHolder.getId() != null) {
            try {
                return accountHolderService.findById(accountHolder.getId());
            } catch (ResponseStatusException e) {
            }
        }
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO(
                accountHolder.getFirstName(),
                accountHolder.getSurname(),
                accountHolder.getDateOfBirth(),
                accountHolder.getPrimaryAddress(),
                accountHolder.getMailingAddress()
        );
        return newAccountHolder(accountHolderDTO);
    }
    // This will update or create an address automatically during AccountHolder Creation
    public Address findOrCreateAddress(Address address) {
        if (address.getId() != null) {
            try {
                return addressService.findById(address.getId());
            } catch (ResponseStatusException e) {
            }
        }
        AddressDTO addressDTO = new AddressDTO(
                address.getHouseNumber(),
                address.getFlatNumber(),
                address.getStreet(),
                address.getTown(),
                address.getCity(),
                address.getPostcode()
        );
        return newAddress(addressDTO);
    }

    // This will update or create Login Details automatically during AccountHolder Creation
    public LoginDetails findOrCreateLoginDetails(LoginDetails loginDetails) {
        if (loginDetails.getId() != null) {
            try {
                return loginDetailsService.findById(loginDetails.getId());
            } catch (ResponseStatusException e) {
            }
        }
        LoginDetailsDTO loginDetailsDTO = new LoginDetailsDTO(
                loginDetails.getUsername(),
                loginDetails.getPassword(),
                loginDetails.getUser()
        );
        return newLoginDetails(loginDetailsDTO);
    }

    // This will decide if a student or checking account should be opened based on age
    public Account newStudentOrChecking(CheckingAccountDTO checkingAccountDTO) {
        if (checkingAccountDTO.getPrimaryHolder() != null && checkingAccountDTO.getSecondaryHolder() != null) {
            checkingAccountDTO.setPrimaryHolder(findOrCreateHolder(checkingAccountDTO.getPrimaryHolder()));
            checkingAccountDTO.setSecondaryHolder(findOrCreateHolder(checkingAccountDTO.getSecondaryHolder()));
            if (checkIfOver24(checkingAccountDTO.getPrimaryHolder()) && checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
                return newCheckingAccount(checkingAccountDTO);
            } else if (!checkIfOver24(checkingAccountDTO.getPrimaryHolder()) && !checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
                return newStudentAccount(checkingAccountDTO);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both holders need to be over or under 24 to open a joint account");
            }
        } else if (checkingAccountDTO.getPrimaryHolder() != null) {
            checkingAccountDTO.setPrimaryHolder(findOrCreateHolder(checkingAccountDTO.getPrimaryHolder()));
            if (checkIfOver24(checkingAccountDTO.getPrimaryHolder())) {
                return newCheckingAccount(checkingAccountDTO);
            } else {
                return newStudentAccount(checkingAccountDTO);
            }
        } else if (checkingAccountDTO.getSecondaryHolder() != null) {
            checkingAccountDTO.setSecondaryHolder(findOrCreateHolder(checkingAccountDTO.getSecondaryHolder()));
            if (checkIfOver24(checkingAccountDTO.getSecondaryHolder())) {
                return newCheckingAccount(checkingAccountDTO);
            } else {
                return newStudentAccount(checkingAccountDTO);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You need a primary holder to open a new account");
    }
}
