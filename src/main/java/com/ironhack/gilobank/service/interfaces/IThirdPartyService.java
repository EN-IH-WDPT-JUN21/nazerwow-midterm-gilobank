package com.ironhack.gilobank.service.interfaces;

import com.ironhack.gilobank.controller.dto.ThirdPartyDTO;
import com.ironhack.gilobank.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.gilobank.dao.ThirdParty;
import com.ironhack.gilobank.dao.Transaction;

public interface IThirdPartyService {

    Transaction creditAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);
    Transaction debitAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);
    Transaction transferBetweenAccounts(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);

    ThirdParty addThirdParty(ThirdPartyDTO thirdPartyDTO);
    ThirdParty updateThirdParty(ThirdPartyDTO thirdPartyDTO);

}
