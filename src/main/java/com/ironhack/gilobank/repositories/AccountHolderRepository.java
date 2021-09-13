package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

}
