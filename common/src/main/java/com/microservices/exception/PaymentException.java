package com.microservices.exception;

public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}