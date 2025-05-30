package com.microservices.common.dtos.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventDto(
        @NotNull Long id,
        @NotNull String name,
        @NotNull Double ticketPrice,
        @NotNull @NotEmpty String place,
        @NotNull LocalDateTime date,
        @NotNull Integer availableTickets,
        @NotNull LocalDateTime createdAt,
        @NotNull LocalDateTime updatedAt
) {
}