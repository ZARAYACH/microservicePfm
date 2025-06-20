package com.microservices.common.dtos.reservation;

import java.time.LocalDateTime;

public record ReservationDto(
        String id,
        Long eventId,
        String userEmail,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
