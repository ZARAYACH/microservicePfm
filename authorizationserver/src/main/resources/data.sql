INSERT IGNORE INTO user
VALUES (1, 'test@test.com', 'test', 'test', '$2a$10$rJETdZGhwXsdzYAYZyvAw.AQBIMQ4GYKbxvI80Y30Rb.KUXAxBxJq',
        '0666666666666666');
INSERT IGNORE INTO user
VALUES (2, 'user@email.com', 'user', 'test', '$2a$10$rJETdZGhwXsdzYAYZyvAw.AQBIMQ4GYKbxvI80Y30Rb.KUXAxBxJq',
        '05785481');
INSERT IGNORE INTO user
VALUES (3, 'organiser@email.com', 'organiser', 'test', '$2a$10$rJETdZGhwXsdzYAYZyvAw.AQBIMQ4GYKbxvI80Y30Rb.KUXAxBxJq',
        '047775211');


INSERT IGNORE INTO authority
VALUES ('ADMIN', 'Admin');
INSERT IGNORE INTO authority
VALUES ('USER', 'normal user');
INSERT IGNORE INTO authority
VALUES ('EVENT_ORGANISER', 'Event Organiser');

INSERT IGNORE INTO user_authority(user_id, authority_id)
VALUES (1, 'USER');
INSERT IGNORE INTO user_authority(user_id, authority_id)
VALUES (1, 'ADMIN');
INSERT IGNORE INTO user_authority(user_id, authority_id)
VALUES (1, 'EVENT_ORGANISER');

INSERT IGNORE INTO user_authority(user_id, authority_id)
VALUES (2, 'USER');
INSERT IGNORE INTO user_authority(user_id, authority_id)
VALUES (3, 'EVENT_ORGANISER');

INSERT IGNORE INTO microservice_pfm_auth_server.client (id, authorization_grant_types, client_authentication_methods,
                                                        client_id, client_id_issued_at, client_name, client_secret,
                                                        client_secret_expires_at, client_settings,
                                                        post_logout_redirect_uris, redirect_uris, scopes,
                                                        token_settings)
VALUES ('6acd453a-e724-4917-bdb1-2621f7a7d9ea', 'refresh_token,client_credentials,authorization_code',
        'client_secret_post,none,client_secret_basic', 'eventService', null, '9276859e-6797-482d-ba86-95705b82bea0',
        '$2a$10$aMSQOc28f6VvvbDniYx2fOg0DiBuEmZbvaNH.YOuV1kvje1MiX/s2', null,
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
        'http://localhost:8080/logged-out', 'http://localhost:8080/authorized', 'user.read,openid,profile,event',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",7776000.000000000]}'),
       ('1da5a3a5-7c6c-47db-ae78-e5ba9d2195f5', 'refresh_token,client_credentials,authorization_code',
        'client_secret_post,none,client_secret_basic', 'paymentService', null, '0bdb7298-e277-4d47-a76d-d54ba6d1b1fb',
        '$2a$10$aMSQOc28f6VvvbDniYx2fOg0DiBuEmZbvaNH.YOuV1kvje1MiX/s2', null,
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
        'http://localhost:8080/logged-out', 'http://localhost:8080/authorized',
        'user.read,openid,profile,event,payment',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",7776000.000000000]}'),
       ('47f39080-ae58-47b5-95fa-c2dc0f487732', 'refresh_token,client_credentials,authorization_code',
        'client_secret_post,none,client_secret_basic', 'reservationService', null,
        '92af4f2d-78d6-41a2-a761-086f8f960dc2', '$2a$10$aMSQOc28f6VvvbDniYx2fOg0DiBuEmZbvaNH.YOuV1kvje1MiX/s2', null,
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
        'http://localhost:8080/logged-out', 'http://localhost:8080/authorized',
        'user.read,openid,profile,event,payment',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",7776000.000000000]}'),
       ('b6e8f7f5-f772-44f5-aaef-e3dd95df8f6d', 'refresh_token,client_credentials,authorization_code',
        'client_secret_post,none,client_secret_basic', 'notificationService', null,
        '1f51c97c-797c-440d-a72f-b0fb3a6af0ed', '$2a$10$aMSQOc28f6VvvbDniYx2fOg0DiBuEmZbvaNH.YOuV1kvje1MiX/s2', null,
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
        'http://localhost:8080/logged-out', 'http://localhost:8080/authorized', 'user.read,openid,profile,event',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",7776000.000000000]}');