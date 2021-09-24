package com.ironhack.gilobank.controller.interfaces;

import com.ironhack.gilobank.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.gilobank.dao.Transaction;

public interface IThirdPartyController {

    Transaction creditAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);

    Transaction debitAccount(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);

    Transaction transferBetweenAccounts(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);

}
