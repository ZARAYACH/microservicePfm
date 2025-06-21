package com.microservices.reservation.controller;

import com.microservices.common.dtos.payment.PaymentWebHookPayloadDto;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.ServiceUnavailableException;
import com.microservices.common.exception.modal.ExceptionDto;
import com.microservices.reservation.modal.Reservation;
import com.microservices.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static com.microservices.common.exception.ControllerAdviceExceptionHandler.buildExceptionDto;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebHookController {

    private final ReservationService reservationService;

    @PostMapping("/payments")
    public void paymentWebHook(@RequestBody PaymentWebHookPayloadDto paymentWebHookPayloadDto) {
        log.debug("Payment WebHook received for payment #" + paymentWebHookPayloadDto.paymentId());
        CompletableFuture.runAsync(() -> {
            Reservation reservation = reservationService.findByPaymentId(paymentWebHookPayloadDto.paymentId());
            try {
                reservationService.signalReservationStatusUpdate(reservation);
            } catch (ServiceUnavailableException | NotFoundException e) {
                buildExceptionDto(e, HttpStatus.SERVICE_UNAVAILABLE);
                log.error("Couldn't signal reservation status update for reservation #" + reservation.getId(), e);
            }
        } );

    }
}
