package com.microservices.payment.security;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JWTSecurityConfig {

    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/process/payments/*",
                                "/api/v1/payments/*/receipt",
                                "/api/v1/process/payments/*/cancel",
                                "/swagger-ui.html",
                                "/swagger-ui/*", "/api/v1/api-docs",
                                "/actuator/health").permitAll()
                        .requestMatchers("/api/v1/payments/**").hasAuthority("SCOPE_payment")
                        .anyRequest().hasAuthority("ROLE_ADMIN"))
                .oauth2ResourceServer(customizer ->
                        customizer.jwt(withDefaults()))
        ;
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        scopeConverter.setAuthoritiesClaimName("scope");
        scopeConverter.setAuthorityPrefix("SCOPE_");

        return new JwtAuthenticationConverter() {{
            setJwtGrantedAuthoritiesConverter(jwt -> {
                Collection<GrantedAuthority> merged = new ArrayList<>();
                merged.addAll(grantedAuthoritiesConverter.convert(jwt));
                merged.addAll(scopeConverter.convert(jwt));
                return merged;
            });
        }};

    }
}