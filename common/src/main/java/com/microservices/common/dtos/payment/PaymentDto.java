package com.microservices.common.dtos.payment;


import com.microservices.common.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentDto(
        String id,
        String cardHolder,
        double amount,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String paymentUrl,
        String webHookUrl
) {

}
