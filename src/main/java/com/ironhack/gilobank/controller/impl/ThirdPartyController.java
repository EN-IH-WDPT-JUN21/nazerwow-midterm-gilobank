package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.gilobank.controller.interfaces.IThirdPartyController;
import com.ironhack.gilobank.dao.ThirdParty;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/thirdparty")
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    private IThirdPartyService thirdPartyService;


    @PutMapping("/{hashedKey}/credit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction creditAccount(@PathVariable(name="hasheKey") String hashedKey,
                                     @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        return thirdPartyService.creditAccount(hashedKey, thirdPartyTransactionDTO);
    }

    @Override
    public Transaction debitAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        return null;
    }

    @Override
    public Transaction transferBetweenAccounts(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        return null;
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
