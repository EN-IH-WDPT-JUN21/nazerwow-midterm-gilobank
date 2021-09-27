package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.CheckingAccount;
import com.ironhack.gilobank.utils.Money;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    @Query("SELECT a.balance FROM CheckingAccount a WHERE a.accountNumber = :account")
    Money getBalanceByAccountNumber(@Param("account") Long accountNumber);


}
