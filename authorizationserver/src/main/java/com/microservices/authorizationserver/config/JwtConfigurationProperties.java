package com.microservices.authorizationserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@EnableConfigurationProperties(JwtConfigurationProperties.class)
@ConfigurationProperties(prefix = "app.security.jwt")
@Configuration
public class JwtConfigurationProperties {
    private String KeyPairsPath = "/home/authServer/keys";

}
