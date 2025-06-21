package com.microservices.common.dtos.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateEventDto(
        @NotNull @NotEmpty String name,
        @NotNull Double ticketPrice,
        @NotNull Integer availableTickets,
        @NotNull @NotEmpty String place,
        @NotNull LocalDateTime date,
        @NotNull @NotEmpty String type
) {
}
