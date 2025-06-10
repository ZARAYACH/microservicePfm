package com.microservices.authorizationserver.config.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.microservices.authorizationserver.modal.Role;
import com.microservices.authorizationserver.modal.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {
    @Primary
    @Bean(value = "objectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Bean(value = "customObjectMapper")
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(User.class, new UserSerializer());
        module.addDeserializer(User.class, new UserDeserializer());
        module.addSerializer(Role.class, new RoleSerializer());
        module.addDeserializer(Role.class, new RoleDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

}

