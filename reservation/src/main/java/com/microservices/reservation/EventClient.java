package com.microservices.reservation;

import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventClient {

    private final WebClient webClient;
    @Value("${services.event.url}")
    private String eventServiceUrl;

    public Optional<EventDto> getEventById(Long id) throws ServiceUnavailableException {
        try {
            return Optional.ofNullable(webClient.get()
                    .uri(eventServiceUrl + "/api/v1/events/{id}", id)
                    .retrieve()
                    .bodyToMono(EventDto.class)
                    .block());
        } catch (Exception e) {
            throw new ServiceUnavailableException(e);
        }

    }
}
