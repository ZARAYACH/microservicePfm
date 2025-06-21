package com.microservices.payment.service;

import com.microservices.common.PaymentStatus;
import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.PaymentException;
import com.microservices.payment.config.PaymentServiceProperties;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.modal.PaymentInfoDto;
import com.microservices.payment.repository.PaymentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentServiceProperties paymentServiceProperties;

    private final static String PAYMENT_URL = "/api/v1/process/payments/";

    public List<Payment> list() {
        return paymentRepository.findAll();
    }

    public Payment findById(String id) throws NotFoundException, BadArgumentException {
        try {
            Assert.isTrue(StringUtils.isNotBlank(id), "Payment id cannot be blank.");
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment with id #" + id + " Not found."));

    }

    public void processPayment(PaymentInfoDto paymentInfoDto, @NotNull Payment payment) throws PaymentException {
        try {
            Assert.isTrue(StringUtils.isNotBlank(paymentInfoDto.cardHolder()), "Card holder name cannot be blank.");
            Assert.isTrue(StringUtils.isNotBlank(paymentInfoDto.cardNumber()), "Card number cannot be blank.");
            Assert.isTrue(StringUtils.isNotBlank(paymentInfoDto.cvv()), "CVV cannot be blank.");
        } catch (IllegalArgumentException e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentException(e);
        }
        payment.setEmail(paymentInfoDto.email());
        payment.setCardHolder(paymentInfoDto.cardHolder());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    public Payment createPayment(@NotNull CreatePaymentDto createPaymentDto) throws BadArgumentException {
        Payment payment = validatePaymentDtoAndCreate(createPaymentDto);
        payment.setPaymentUrl(paymentServiceProperties.getRootUrl() + PAYMENT_URL + payment.getId());
        return paymentRepository.save(payment);

    }

    private Payment validatePaymentDtoAndCreate(@NotNull CreatePaymentDto createPaymentDto) throws BadArgumentException {
        try {
            Assert.isTrue(createPaymentDto.amount() > 0, "Amount must be greater than zero.");
            Assert.isTrue(StringUtils.isNotBlank(createPaymentDto.orderId()), "Card holder name cannot be blank.");
            Assert.isTrue(StringUtils.isNotBlank(createPaymentDto.callbackUrl()), "CallbackUrl cannot be null or empty");
            Assert.isTrue(StringUtils.isNotBlank(createPaymentDto.webHookUrl()), "WebhookUrl cannot be null or empty");
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }

        return Payment.builder()
                .id(UUID.randomUUID().toString())
                .amount(createPaymentDto.amount())
                .callbackUrl(createPaymentDto.callbackUrl())
                .orderId(createPaymentDto.orderId())
                .status(PaymentStatus.PENDING)
                .webHookUrl(createPaymentDto.webHookUrl())
                .build();
    }

    public void delete(@NotNull Payment payment) {
        paymentRepository.delete(payment);
    }

    public void validatePaymentStatusForProcessing(@NotNull Payment payment) throws PaymentException {
        try {
            Assert.isTrue(payment.getStatus() != null && payment.getStatus().equals(PaymentStatus.PENDING), "Payment already processed or failed.");
            Assert.isTrue(StringUtils.isNotBlank(payment.getPaymentUrl()), "Payment url cannot be null or empty.");
            Assert.isTrue(StringUtils.isNotBlank(payment.getOrderId()), "Order id cannot be null or empty.");
            Assert.isTrue(StringUtils.isNotBlank(payment.getCallbackUrl()), "Callback url cannot be null or empty.");
            Assert.isTrue(payment.getCreatedAt().isBefore(LocalDateTime.now().plusMinutes(15)), "Payment has expired.");
        } catch (IllegalArgumentException e) {
            throw new PaymentException(e);
        }
    }

    public void cancelPayment(Payment payment) {
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
    }
}
