package com.microservices.authorizationserver.repository;

import com.microservices.authorizationserver.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAddress(String email);
}
