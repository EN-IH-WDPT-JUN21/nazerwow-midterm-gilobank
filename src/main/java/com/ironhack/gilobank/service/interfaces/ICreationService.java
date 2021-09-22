package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.*;
import com.ironhack.gilobank.dao.*;

public interface ICreationService {

    Address newAddress(AddressDTO addressDTO);

    AccountHolder newAccountHolder(AccountHolderDTO accountHolderDTO);

    LoginDetails newLoginDetails(LoginDetailsDTO loginDetailsDTO);

    CheckingAccount newCheckingAccount(CheckingAccountDTO checkingAccountDTO);

    StudentAccount newStudentAccount(CheckingAccountDTO checkingAccountDTO);

//    Account createAccount(AccountDTO accountDTO);

    boolean checkIfOver24(AccountHolder accountHolder);




}
