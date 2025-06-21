package com.microservices.common.dtos.payment;

public record CreatePaymentDto(
        double amount,
        String orderId,
        String callbackUrl,
        String webHookUrl
) {
}
