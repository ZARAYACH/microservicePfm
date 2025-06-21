package com.microservices.payment.service;

import com.microservices.common.PaymentStatus;
import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.PaymentException;
import com.microservices.payment.config.PaymentServiceProperties;
import com.microservices.payment.mapper.PaymentMapper;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class MockPaymentServiceTest {
    @Mock
    private PaymentRepository repository;

    @Mock
    private PaymentMapper mapper;

    @Mock
    private PaymentServiceProperties paymentServiceProperties;

    @InjectMocks
    private PaymentService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPayment_shouldSucceed_withValidCard() throws PaymentException, BadArgumentException {
        CreatePaymentDto dto = new CreatePaymentDto(44, "test-01", "http://localhost:8080/callbadck", "http://localhost:8080/webhook");
        Payment mockEntity = new Payment("1", "flan", "flan@email.com", 44, "test-01", "http://localhost:8080/callbadck", PaymentStatus.PENDING, "http://localhost:8080/payment", "http://localhost:8080/webhook", LocalDateTime.now(), LocalDateTime.now());
        when(repository.save(any(Payment.class))).thenReturn(mockEntity);
        Payment result = service.createPayment(dto);
        assertNotNull(result);
    }

    @Test
    void processPayment_shouldFail_withInvalidCard() {
        CreatePaymentDto dto = new CreatePaymentDto(0, "test-01", "http://localhost:8080/callbadck", "http://localhost:8080/webhook");
        assertThrows(BadArgumentException.class, () -> service.createPayment(dto));
    }

    @Test
    void getById_shouldReturnResponseIfExists() throws BadArgumentException, NotFoundException {
        Payment payment = new Payment();
        when(repository.findById("1")).thenReturn(Optional.of(payment));
        Payment result = service.findById("1");
        assertNotNull(result);
    }
}
