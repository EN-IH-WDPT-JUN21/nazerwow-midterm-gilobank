package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
    @Query("SELECT ca FROM StudentAccount ca WHERE " +
            "(ca.primaryHolder.loginDetails.id = ?#{principal.id}) " +
            "OR (ca.secondaryHolder.loginDetails.id = ?#{principal.id})" +
            "or (1=?#{hasRole('ROLE_ADMIN') ? 1 : 0})")
    List<StudentAccount> findAllSecure();

    @Query("SELECT ca FROM StudentAccount ca WHERE " +
            "(ca.accountNumber = :accountNumber) AND " +
            "(ca.primaryHolder.loginDetails.id = ?#{principal.id}) " +
            "OR (ca.secondaryHolder.loginDetails.id = ?#{principal.id})" +
            "or (1=?#{hasRole('ROLE_ADMIN') ? 1 : 0})")
    Optional<StudentAccount> findByAccountNumberSecure(@Param("accountNumber") Long accountNumber);
}
