package com.microservices.authorizationserver.repository;

import com.microservices.authorizationserver.modal.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, String> {
}
