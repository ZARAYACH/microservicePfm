package com.microservices.reservation.http;

import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.dtos.payment.PaymentDto;
import com.microservices.common.exception.ServiceUnavailableException;
import com.microservices.reservation.config.ReservationServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentClient {

    private final WebClient webClient;
    private final ReservationServiceProperties reservationServiceProperties;
    private final String PAYMENT_API_PREFIX = "/api/v1/payments";

    public Optional<PaymentDto> createPayment(CreatePaymentDto createPaymentDto) throws ServiceUnavailableException {
        try {
            return Optional.ofNullable(webClient.post()
                    .uri(reservationServiceProperties.getPaymentServiceUrl() +
                            "/api/v1/payments")
                    .body(Mono.just(createPaymentDto), CreatePaymentDto.class)
                    .retrieve()
                    .bodyToMono(PaymentDto.class)
                    .block());
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }

    public Optional<PaymentDto> findPaymentById(String id) throws ServiceUnavailableException {
        try {
            return Optional.ofNullable(webClient.get()
                    .uri(String.format(reservationServiceProperties.getPaymentServiceUrl() + PAYMENT_API_PREFIX + "/%s", id))
                    .retrieve()
                    .bodyToMono(PaymentDto.class)
                    .block());
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }
}
