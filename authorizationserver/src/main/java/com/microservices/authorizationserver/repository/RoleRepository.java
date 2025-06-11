package com.microservices.authorizationserver.repository;

import com.microservices.authorizationserver.modal.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Authority, String> {
}
