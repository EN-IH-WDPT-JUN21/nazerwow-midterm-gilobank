package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT t FROM Transaction t WHERE account = :account AND t.timeOfTrns  BETWEEN :startDate AND :endDate ORDER BY t.timeOfTrns")
    List<Transaction> findByTimeOfTrnsBetween(@Param("account") Account account,
                                              @Param("startDate") LocalDateTime startPoint,
                                              @Param("endDate") LocalDateTime endPoint);

    @Query(value = "SELECT * FROM transaction WHERE account_id = :account AND time_of_trns > DATE_SUB(NOW(), INTERVAL 24 HOUR)", nativeQuery = true)
    List<Transaction> findTrnsInLast24Hour(@Param("account") Account account);

    @Query(value = "SELECT SUM(amount) FROM transaction WHERE account_id = :account AND time_of_trns > DATE_SUB(NOW(), INTERVAL 24 HOUR)", nativeQuery = true)
    BigDecimal getValueOfLast24Hour(@Param("account") Account account);

//    @Query(value = "SELECT amount, time_of_trns, account_id FROM transaction WHERE account_id = :account", nativeQuery = true)
//    List<Transaction> getTotalValueGroupByDay(@Param("account") Account account);

}
