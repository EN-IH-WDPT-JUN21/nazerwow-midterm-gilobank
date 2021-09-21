package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.CheckingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Long> {

    @Query("SELECT ca FROM CheckingAccount ca WHERE " +
            "ca.primaryHolder.loginDetails.id = ?#{principal.id} " +
            "OR ca.secondaryHolder.loginDetails.id = ?#{principal.id}" +
            "OR 2=?#{hasRole('ROLE_ADMIN') ? 2 : 0}")
    List<CheckingAccount> findAllSecure();

    @Query("SELECT ca FROM CheckingAccount ca WHERE " +
            "(ca.accountNumber = :accountNumber) AND " +
            "(ca.primaryHolder.loginDetails.id = ?#{principal.id}) " +
            "OR (ca.secondaryHolder.loginDetails.id = ?#{principal.id})" +
            "or (1=?#{hasRole('ROLE_ADMIN') ? 1 : 0})")
    Optional<CheckingAccount> findByAccountNumberSecure(@Param("accountNumber") Long accountNumber);

//    @Query("SELECT ca FROM CheckingAccount ca WHERE or 1=?#{hasRole('ROLE_ADMIN') ? 1 : 0}")
//    List<CheckingAccount> findAll();

}
