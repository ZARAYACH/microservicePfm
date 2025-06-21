package com.microservices.payment;

import com.microservices.common.dtos.payment.PaymentWebHookPayloadDto;
import com.microservices.payment.modal.Payment;
import com.microservices.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;


@Component
@RequiredArgsConstructor
@Slf4j
public class StatusChangeEventListener implements ApplicationListener<StatusChangeEvent> {

    private final PaymentService paymentService;
    private final WebClient webClient;

    @Override
    @Transactional
    public void onApplicationEvent(StatusChangeEvent event) {
        Payment payment = event.getPayment();
        try {
            handleStatusChange(payment);
        } catch (Exception e) {
            log.error("Couldn't handle status change event for order #" + payment.getId(), e);
        }
    }

    private void handleStatusChange(Payment payment) {
        log.debug("Status change event received for payment #" + payment.getId() + " with status " + payment.getStatus());
        webClient.post()
                .uri(payment.getWebHookUrl())
                .body(Mono.just(new PaymentWebHookPayloadDto(payment.getId())), PaymentWebHookPayloadDto.class)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), ClientResponse::createException)
                .bodyToMono(Void.class)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(5)))
                .doOnError(ex -> log.error("Webhook call failed after retries", ex))
                .timeout(Duration.ofSeconds(10))
                .block();
    }
}
