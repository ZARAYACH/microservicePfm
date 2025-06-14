package com.microservices.authorizationserver.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "`client`")
@Getter
@Setter
public class Client {
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    @Column(columnDefinition = "LONGTEXT")
    private String clientAuthenticationMethods;
    @Column(columnDefinition = "LONGTEXT")
    private String authorizationGrantTypes;
    @Column(columnDefinition = "LONGTEXT")
    private String redirectUris;
    @Column(columnDefinition = "LONGTEXT")
    private String postLogoutRedirectUris;
    @Column(columnDefinition = "LONGTEXT")
    private String scopes;
    @Column(columnDefinition = "LONGTEXT")
    private String clientSettings;
    @Column(columnDefinition = "LONGTEXT")
    private String tokenSettings;

}