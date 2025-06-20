package com.microservices.common.dtos.reservation;

public record CreateReservationDto(
        long eventId,
        int quantity
) {
}
