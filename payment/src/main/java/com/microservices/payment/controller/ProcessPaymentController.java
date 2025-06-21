package com.microservices.payment.controller;

import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.PaymentException;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.modal.PaymentInfoDto;
import com.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/process/payments")
@RequiredArgsConstructor
@Slf4j
public class ProcessPaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public String processPaymentPage(@PathVariable String id, Model model) throws BadArgumentException, NotFoundException, PaymentException {
        Payment payment = paymentService.findById(id);
        paymentService.validatePaymentStatusForProcessing(payment);

        model.addAttribute("payment", payment);
        return "payment-page";
    }

    @PostMapping("/{id}")
    public String processPayment(@PathVariable String id, @ModelAttribute PaymentInfoDto paymentInfoDto, Model model) throws BadArgumentException, NotFoundException, PaymentException {
        Payment payment = paymentService.findById(id);
        paymentService.validatePaymentStatusForProcessing(payment);
        paymentService.processPayment(paymentInfoDto, payment);
        model.addAttribute("payment", payment);
        return "payment-page-success";
    }

    @GetMapping("/{id}/cancel")
    public String cancelPayment(@PathVariable String id, Model model) throws BadArgumentException, NotFoundException, PaymentException {
        Payment payment = paymentService.findById(id);
        paymentService.validatePaymentStatusForProcessing(payment);
        paymentService.cancelPayment(payment);
        model.addAttribute("payment", payment);
        return "payment-page-success";
    }
}
