package com.microservices.payment;

import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.repository.PaymentRepository;
import com.microservices.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentApplicationTests {

    @MockitoBean
    ClientRegistrationRepository ClientRegistrationRepository;
    @MockitoBean
    OAuth2AuthorizedClientRepository OAuth2AuthorizedClientRepository;

    @Autowired
    private PaymentService service;
    @Autowired
    private PaymentRepository repository;

    @Test
    void contextLoads() {
    }

    @Test
    void testSuccessfulPaymentProcessing() throws Exception {
        CreatePaymentDto dto = new CreatePaymentDto(44, "test-01", "http://localhost:8080/callbadck", "http://localhost:8080/webhook");

        Payment response = service.createPayment(dto);

        assertNotNull(response);

        Optional<Payment> saved = repository.findAll().stream().findFirst();
        assertTrue(saved.isPresent());
        assertEquals("test-01", saved.get().getOrderId());
    }

    @Test
    void testGetByIdReturnsSavedPayment() throws Exception {
        CreatePaymentDto dto = new CreatePaymentDto(44, "test-01", "http://localhost:8080/callbadck", "http://localhost:8080/webhook");

        Payment response = service.createPayment(dto);
        Payment saved = repository.findAll().get(0);

        Optional<Payment> loaded = repository.findById(saved.getId());
        assertTrue(loaded.isPresent());
        assertEquals(saved.getId(), loaded.get().getId());
    }
}
