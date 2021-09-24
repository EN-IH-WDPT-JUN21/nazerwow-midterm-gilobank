package com.ironhack.gilobank.controller.impl;

import com.ironhack.gilobank.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.gilobank.controller.interfaces.IThirdPartyController;
import com.ironhack.gilobank.dao.Transaction;
import com.ironhack.gilobank.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thirdparty")
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    private IThirdPartyService thirdPartyService;


    @PutMapping("/{hashedKey}/credit")
    @ResponseStatus(HttpStatus.OK)
    public Transaction creditAccount(@PathVariable(name = "hashedKey") String hashedKey,
                                     @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        return thirdPartyService.creditAccount(hashedKey, thirdPartyTransactionDTO);
    }

    @PutMapping("/{hashedKey}/debit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transaction debitAccount(@PathVariable(name = "hashedKey") String hashedKey,
                                    @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        return thirdPartyService.debitAccount(hashedKey, thirdPartyTransactionDTO);
    }

    @PutMapping("/{hashedKey}/transfer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transaction transferBetweenAccounts(@PathVariable(name = "hashedKey") String hashedKey,
                                               @RequestBody ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        return thirdPartyService.transferBetweenAccounts(hashedKey, thirdPartyTransactionDTO);
    }

}
