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
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ThirdPartyService implements IThirdPartyService {


    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private ITransactionService transactionService;

    public ThirdParty findById(Long id){
        Optional<ThirdParty> optionalThirdParty=  thirdPartyRepository.findById(id);
        if(optionalThirdParty.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Third Party found with id: " + id);
        }
        return optionalThirdParty.get();
    }

    @Override
    public Transaction creditAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if(transactionService.verifyThirdParty(hashedKey) &&
                transactionService.verifySecretKey(
                        thirdPartyTransactionDTO.getCreditAccountSecretKey(),
                        transactionService.findAccountTypeAndReturn(thirdPartyTransactionDTO.getCreditAccountNumber()))){
            TransactionDTO transactionDTO = new TransactionDTO(
                    thirdPartyTransactionDTO.getCreditAccountNumber(),
                    thirdPartyTransactionDTO.getAmount(),
                    TransactionType.CREDIT);
            return transactionService.creditFunds(transactionDTO);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public Transaction debitAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if(transactionService.verifyThirdParty(hashedKey) &&
                transactionService.verifySecretKey(
                        thirdPartyTransactionDTO.getDebitAccountSecretKey(),
                        transactionService.findAccountTypeAndReturn(thirdPartyTransactionDTO.getDebitAccountNumber()))){
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setAmount(thirdPartyTransactionDTO.getAmount());
            transactionDTO.setDebitAccountNumber(thirdPartyTransactionDTO.getDebitAccountNumber());
            transactionDTO.setType(TransactionType.DEBIT);
            return transactionService.debitFunds(transactionDTO);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public Transaction transferBetweenAccounts(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if(transactionService.verifyThirdParty(hashedKey) &&
                transactionService.verifySecretKey(thirdPartyTransactionDTO.getDebitAccountSecretKey(),
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

    public void saveThirdParty(ThirdParty thirdParty){
        thirdPartyRepository.save(thirdParty);
    }
}
