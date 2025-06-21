package com.microservices.reservation.http;

import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.exception.ServiceUnavailableException;
import com.microservices.reservation.config.ReservationServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventClient {

    private final WebClient webClient;
    private final ReservationServiceProperties reservationServiceProperties;

    public Optional<EventDto> getEventById(Long id) throws ServiceUnavailableException {
        try {
            return Optional.ofNullable(webClient.get()
                    .uri(reservationServiceProperties.getEventServiceUrl() + "/api/v1/events/{id}", id)
                    .retrieve()
                    .bodyToMono(EventDto.class)
                    .block());
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }
    }
}
