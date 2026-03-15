package com.example.Backend.repository;

import com.example.Backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository for the accounts table. Provides CRUD operations for student accounts.
// Use this to get the account ID for a given user.
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
}
