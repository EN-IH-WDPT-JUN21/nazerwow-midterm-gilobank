package com.ironhack.gilobank.repositories;

import com.ironhack.gilobank.dao.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {

    Optional<LoginDetails> findByUsername(String username);
}
