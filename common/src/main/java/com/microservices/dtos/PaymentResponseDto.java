package com.microservices.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private String id;
    private String cardHolder;
    private double amount;
    private boolean success;
    private String message;
    private String timestamp;



}
