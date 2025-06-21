package com.microservices.payment.controller;

import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.dtos.payment.PaymentDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.PaymentException;
import com.microservices.payment.mapper.PaymentMapper;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PaymentControllerTest {
    @Mock
    PaymentMapper paymentMapper;
    @Mock
    private PaymentService service;

    @InjectMocks
    private PaymentController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPayment_success() throws PaymentException, BadArgumentException {
        CreatePaymentDto request = new CreatePaymentDto(22, "test-1", "http://localhost:8080/callbadck", "http://localhost:8080/webhook");
        Payment expectedResponse = new Payment();

        when(service.createPayment(request)).thenReturn(expectedResponse);

        PaymentDto response = controller.createPayment(request);

        verify(service).createPayment(request);
    }

    @Test
    void getPayment_success() throws PaymentException, BadArgumentException, NotFoundException {
        String paymentId = "abc123";
        Payment expectedResponse = new Payment();

        when(service.findById(paymentId)).thenReturn(expectedResponse);

        PaymentDto response = controller.getPayment(paymentId);

        assertEquals(paymentMapper.toPaymentDto(expectedResponse), response);
        verify(service).findById(paymentId);
    }

    @Test
    void getPayment_notFound() throws BadArgumentException, NotFoundException {
        String paymentId = "unknown";
        when(service.findById(paymentId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> controller.getPayment(paymentId));

        verify(service).findById(paymentId);
    }
}
