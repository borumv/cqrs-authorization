package com.example.authservice.core.repository;

import com.example.authservice.core.entity.UserLookup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLookUpRepository extends JpaRepository<UserLookup, String> {
    UserLookup findByEmail(String email);
}
