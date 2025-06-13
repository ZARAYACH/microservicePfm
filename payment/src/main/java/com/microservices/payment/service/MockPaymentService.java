package com.microservices.payment.service;

import com.microservices.exception.PaymentException;
import com.microservices.dtos.PaymentRequestDto;
import com.microservices.dtos.PaymentResponseDto;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.mapper.PaymentMapper;
import com.microservices.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class MockPaymentService {

    @Value("${mock.card.prefix:4}")
    private String acceptedCardPrefix;

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    @Autowired
    public MockPaymentService(PaymentRepository repository, PaymentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public PaymentResponseDto processPayment(PaymentRequestDto request) throws PaymentException {
        if (request.getCardNumber() == null || !request.getCardNumber().startsWith(acceptedCardPrefix)) {
            throw new PaymentException("Payment failed: invalid card prefix.");
        }
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setCardNumber(request.getCardNumber());
        payment.setAmount(request.getAmount());
        payment.setSuccess(true);
        payment.setMessage("Mock payment successful");

        repository.save(payment);
        return mapper.toDto(payment);
    }

    public Optional<PaymentResponseDto> getById(String id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }
}
