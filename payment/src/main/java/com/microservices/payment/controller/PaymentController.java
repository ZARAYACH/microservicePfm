package com.microservices.payment.controller;

import com.microservices.common.dtos.DeleteResponse;
import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.dtos.payment.PaymentDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.PaymentException;
import com.microservices.payment.mapper.PaymentMapper;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping
    public List<PaymentDto> listPayments() {
        return paymentMapper.toPaymentDto(paymentService.list());
    }

    @GetMapping("/{id}")
    public PaymentDto getPayment(@PathVariable String id) throws BadArgumentException, NotFoundException {
        return paymentMapper.toPaymentDto(paymentService.findById(id));
    }
 
    @PostMapping
    public PaymentDto createPayment(@RequestBody CreatePaymentDto createPaymentDto) throws PaymentException, BadArgumentException {
        return paymentMapper.toPaymentDto(paymentService.createPayment(createPaymentDto));
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deletePayment(@PathVariable String id) throws NotFoundException, BadArgumentException {
        Payment payment = paymentService.findById(id);
        paymentService.delete(payment);
        return new DeleteResponse(true);
    }

}
