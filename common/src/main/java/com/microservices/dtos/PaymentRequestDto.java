package com.microservices.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvc;
    private double amount;
}
