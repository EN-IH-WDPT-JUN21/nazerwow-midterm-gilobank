package com.ironhack.gilobank.service.impl;

import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.gilobank.controller.dto.TransactionDTO;
import com.ironhack.gilobank.dao.ThirdParty;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.enums.TransactionType;
import com.ironhack.gilobank.repositories.ThirdPartyRepository;
import com.ironhack.gilobank.service.interfaces.ICreationService;
import com.ironhack.gilobank.service.interfaces.IThirdPartyService;
import com.ironhack.gilobank.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class ThirdPartyService implements IThirdPartyService {


    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private ITransactionService transactionService;

    @Override
    public Transaction creditAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if(transactionService.verifyThirdParty(hashedKey) &&
                transactionService.verifySecretKey(hashedKey,
                        transactionService.findAccountTypeAndReturn(thirdPartyTransactionDTO.getCreditAccountNumber()))){
            TransactionDTO transactionDTO = new TransactionDTO(
                    thirdPartyTransactionDTO.getCreditAccountNumber(),
                    thirdPartyTransactionDTO.getAmount(),
                    TransactionType.CREDIT);
            return transactionService.creditFunds(transactionDTO);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Transaction debitAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if(transactionService.verifyThirdParty(hashedKey) &&
                transactionService.verifySecretKey(hashedKey,
                        transactionService.findAccountTypeAndReturn(thirdPartyTransactionDTO.getDebitAccountNumber()))){
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setAmount(thirdPartyTransactionDTO.getAmount());
            transactionDTO.setDebitAccountNumber(thirdPartyTransactionDTO.getDebitAccountNumber());
            transactionDTO.setType(TransactionType.DEBIT);
            return transactionService.debitFunds(transactionDTO);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public Transaction transferBetweenAccounts(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if(transactionService.verifyThirdParty(hashedKey) &&
                transactionService.verifySecretKey(hashedKey,
                        transactionService.findAccountTypeAndReturn(thirdPartyTransactionDTO.getDebitAccountNumber()))){
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setAmount(thirdPartyTransactionDTO.getAmount());
            transactionDTO.setDebitAccountNumber(thirdPartyTransactionDTO.getDebitAccountNumber());
            transactionDTO.setCreditAccountNumber(thirdPartyTransactionDTO.getCreditAccountNumber());
            return transactionService.transferBetweenAccounts(transactionDTO);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ThirdParty addThirdParty(ThirdPartyDTO thirdPartyDTO) {
        return null;
    }

    @Override
    public ThirdParty updateThirdParty(ThirdPartyDTO thirdPartyDTO) {
        return null;
    }
}
