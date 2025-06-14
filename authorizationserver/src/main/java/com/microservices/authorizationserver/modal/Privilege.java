package com.microservices.authorizationserver.modal;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "privilege")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Privilege implements GrantedAuthority {

    @Id
    private String id;
    private String description;

    @ManyToMany
    private Set<User> privileges = new HashSet<>();

    @Override
    public String getAuthority() {
        return id;
    }
}
