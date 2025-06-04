package com.microservices.common.exception.modal;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.random.RandomGenerator;

@Getter
@RequiredArgsConstructor
@Builder
public class ExceptionDto {

    @NotNull private final LocalDateTime timestamp = LocalDateTime.now();
    @NotNull private final String message;
    @NotNull private final int status;
    @NotNull private final String statusDescription;
    @NotNull private final String errorId = RandomStringUtils.random(10, 0, 0, true, true, null, Random.from(RandomGenerator.getDefault()));

}
