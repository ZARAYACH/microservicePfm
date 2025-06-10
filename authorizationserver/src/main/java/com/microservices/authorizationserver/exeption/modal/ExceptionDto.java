package com.microservices.authorizationserver.exeption.modal;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ExceptionDto {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String errorId = RandomStringUtils.randomAlphanumeric(8);
    private final List<String> message;

    public ExceptionDto(List<String> message) {
        this.message = message;
    }
}
