package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    @Query("SELECT a.balance FROM CheckingAccount a WHERE a.accountNumber = :account")
    BigDecimal getBalanceByAccountNumber(@Param("account") Long accountNumber);


}
