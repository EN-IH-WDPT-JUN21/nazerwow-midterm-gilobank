package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    @Query("SELECT ca FROM SavingsAccount ca WHERE " +
            "(ca.primaryHolder.loginDetails.id = ?#{principal.id}) " +
            "OR (ca.secondaryHolder.loginDetails.id = ?#{principal.id})" +
            "or (1=?#{hasRole('ROLE_ADMIN') ? 1 : 0})")
    List<SavingsAccount> findAllSecure();

    @Query("SELECT ca FROM SavingsAccount ca WHERE " +
            "(ca.accountNumber = :accountNumber) AND " +
            "(ca.primaryHolder.loginDetails.id = ?#{principal.id}) " +
            "OR (ca.secondaryHolder.loginDetails.id = ?#{principal.id})" +
            "or (1=?#{hasRole('ROLE_ADMIN') ? 1 : 0})")
    Optional<SavingsAccount> findByAccountNumberSecure(@Param("accountNumber") Long accountNumber);
}
