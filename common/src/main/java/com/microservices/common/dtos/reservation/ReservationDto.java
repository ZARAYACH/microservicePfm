package com.microservices.common.dtos.reservation;

import java.time.LocalDateTime;

public record ReservationDto(
        String id,
        Long eventId,
        String userEmail,
        String status,
        String paymentId,
        String paymentUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
