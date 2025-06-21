package com.microservices.payment.controller;

import com.microservices.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PaymentControllerTest {
    @Mock
    private PaymentService service;

    @InjectMocks
    private PaymentController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//
//    @Test
//    void processPayment_success() throws PaymentException {
//        PaymentRequestDto request = new PaymentRequestDto("1234123412341234","test name","xx-xx-xxxX","000",1000.0);
//        PaymentResponseDto expectedResponse = new PaymentResponseDto("1","test name",1000.0,true,"Mock payment successful","xx-xx-xxxx");
//
//        when(service.processPayment(request)).thenReturn(expectedResponse);
//
//        PaymentResponseDto response = controller.processPayment(request);
//
//        assertEquals(expectedResponse, response);
//        verify(service).processPayment(request);
//    }
//
//    @Test
//    void getPayment_success() throws PaymentException {
//        String paymentId = "abc123";
//        PaymentResponseDto expectedResponse = new PaymentResponseDto("abc123","test name",1000.0,true,"Mock payment successful","xx-xx-xxxx");
//
//        when(service.getById(paymentId)).thenReturn(Optional.of(expectedResponse));
//
//        PaymentResponseDto response = controller.getPayment(paymentId);
//
//        assertEquals(expectedResponse, response);
//        verify(service).getById(paymentId);
//    }
//
//    @Test
//    void getPayment_notFound() {
//        String paymentId = "unknown";
//        when(service.getById(paymentId)).thenReturn(Optional.empty());
//
//        PaymentException exception = assertThrows(PaymentException.class, () -> {
//            controller.getPayment(paymentId);
//        });
//
//        assertEquals("Payment not found", exception.getMessage());
//        verify(service).getById(paymentId);
//    }
}
