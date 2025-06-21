package com.microservices.payment.modal;

public record PaymentInfoDto(
        String email,
        String cardHolder,
        String cardNumber,
        String cvv
) {
}
