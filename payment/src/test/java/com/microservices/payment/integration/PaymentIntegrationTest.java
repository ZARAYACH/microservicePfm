package com.microservices.payment.integration;
import com.microservices.payment.repository.PaymentRepository;
import com.microservices.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class PaymentIntegrationTest {
    @Autowired
    private PaymentService service;

    @Autowired
    private PaymentRepository repository;
//
//    @Test
//    void testSuccessfulPaymentProcessing() throws Exception {
//        PaymentRequestDto dto = new PaymentRequestDto();
//        dto.setCardNumber("4111222233334444");
//        dto.setAmount(50.0);
//
//        PaymentResponseDto response = service.processPayment(dto);
//
//        assertTrue(response.isSuccess());
//        assertEquals("Mock payment successful", response.getMessage());
//
//        Optional<Payment> saved = repository.findAll().stream().findFirst();
//        assertTrue(saved.isPresent());
//        assertEquals("4111222233334444", saved.get().getCardNumber());
//    }
//
//    @Test
//    void testGetByIdReturnsSavedPayment() throws Exception {
//        PaymentRequestDto dto = new PaymentRequestDto();
//        dto.setCardNumber("4111222233334444");
//        dto.setAmount(75.0);
//
//        PaymentResponseDto response = service.processPayment(dto);
//        Payment saved = repository.findAll().get(0);
//
//        Optional<Payment> loaded = repository.findById(saved.getId());
//        assertTrue(loaded.isPresent());
//        assertEquals(saved.getCardNumber(), loaded.get().getCardNumber());
//    }
}
