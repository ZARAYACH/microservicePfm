package com.microservices.authorizationserver.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "`authorization`")
@Getter
@Setter
public class Authorization {
    @Id
    @Column
    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    @Column(columnDefinition = "LONGTEXT")
    private String authorizedScopes;
    @Column(columnDefinition = "LONGTEXT")
    private String attributes;
    @Column(columnDefinition = "LONGTEXT")
    private String state;

    @Column(columnDefinition = "LONGTEXT")
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;
    private String authorizationCodeMetadata;

    @Column(columnDefinition = "LONGTEXT")
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    @Column(columnDefinition = "LONGTEXT")
    private String accessTokenMetadata;
    private String accessTokenType;
    @Column(columnDefinition = "LONGTEXT")
    private String accessTokenScopes;

    @Column(columnDefinition = "LONGTEXT")
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;
    @Column(columnDefinition = "LONGTEXT")
    private String refreshTokenMetadata;

    @Column(columnDefinition = "LONGTEXT")
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;
    @Column(columnDefinition = "LONGTEXT")
    private String oidcIdTokenMetadata;
    @Column(columnDefinition = "LONGTEXT")
    private String oidcIdTokenClaims;

    @Column(columnDefinition = "LONGTEXT")
    private String userCodeValue;
    private Instant userCodeIssuedAt;
    private Instant userCodeExpiresAt;
    @Column(length = 2000)
    private String userCodeMetadata;

    @Column(columnDefinition = "LONGTEXT")
    private String deviceCodeValue;
    private Instant deviceCodeIssuedAt;
    private Instant deviceCodeExpiresAt;
    @Column(columnDefinition = "LONGTEXT")
    private String deviceCodeMetadata;
}