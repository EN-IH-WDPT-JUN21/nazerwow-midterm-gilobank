package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.Account;
import com.ironhack.gilobank.dao.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT t FROM Transaction t WHERE account = :account AND t.timeOfTrns  BETWEEN :startDate AND :endDate ORDER BY t.timeOfTrns")
    List<Transaction> findByTimeOfTrnsBetween(@Param("account") Account account,
                                              @Param("startDate") LocalDateTime startPoint,
                                              @Param("endDate") LocalDateTime endPoint);

    @Query(value = "SELECT * FROM transaction WHERE account_id = :account AND type LIKE '%DEBIT%' AND time_of_trns > DATE_SUB(NOW(), INTERVAL 24 HOUR)", nativeQuery = true)
    List<Transaction> allDebitsFromLast24Hour(@Param("account") Account account);

    @Query(value = "SELECT SUM(amount) FROM transaction WHERE account_id = :account AND type LIKE '%debit%' AND time_of_trns > DATE_SUB(NOW(), INTERVAL 24 HOUR)", nativeQuery = true)
    BigDecimal totalOfAllDebitsFromLast24Hours(@Param("account") Account account);

    @Query(value = "SELECT SUM(amount) FROM transaction WHERE account_id = :account AND type LIKE '%debit%' GROUP BY DATE(time_of_trns)", nativeQuery = true)
    List<BigDecimal> historicDailyTotals(@Param("account") Account account);

    @Query(value = "SELECT time_of_trns as time FROM transaction " +
            "WHERE account_id = :account " +
            "AND type LIKE '%debit%' " +
            "AND time_of_trns > DATE_SUB(NOW(), INTERVAL 24 HOUR) " +
            "GROUP BY time_of_trns, DATE(time_of_trns), HOUR(time_of_trns), MINUTE(time_of_trns), SECOND(time_of_trns) " +
            "HAVING COUNT(time_of_trns) > 1 ORDER BY time;", nativeQuery = true)
    List<Timestamp> debitsWithin1Second(@Param("account") Account account);

    @Query(value = "SELECT time_of_trns FROM transaction WHERE account_id = :account AND type LIKE '%INTEREST%' AND time_of_trns > DATE_SUB(NOW(), INTERVAL 1 MONTH)", nativeQuery = true)
    List<Timestamp> interestMonth(@Param("account") Account account);

    @Query(value = "SELECT time_of_trns FROM transaction WHERE account_id = :account AND type LIKE '%INTEREST%' AND time_of_trns > DATE_SUB(NOW(), INTERVAL 1 YEAR)", nativeQuery = true)
    List<Timestamp> interestYear(@Param("account") Account account);

}
