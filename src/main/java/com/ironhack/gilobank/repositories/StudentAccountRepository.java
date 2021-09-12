package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.StudentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccount, Long> {
}
