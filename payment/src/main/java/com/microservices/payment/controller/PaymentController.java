package com.microservices.payment.controller;

import com.microservices.dtos.PaymentRequestDto;
import com.microservices.dtos.PaymentResponseDto;
import com.microservices.exception.PaymentException;
import com.microservices.payment.service.MockPaymentService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock-payments")
public class PaymentController {

    private final MockPaymentService service;

    public PaymentController(MockPaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResponseDto processPayment(@RequestBody PaymentRequestDto request) throws PaymentException {
        return service.processPayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getPayment(@PathVariable String id) throws PaymentException {
        return service.getById(id)
                .orElseThrow(() -> new PaymentException("Payment not found"));
    }
}
