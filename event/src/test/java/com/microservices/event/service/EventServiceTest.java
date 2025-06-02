package com.microservices.event.service;

import com.microservices.common.dtos.event.CreateEventDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.event.EventRepository;
import com.microservices.event.modal.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class EventServiceTest {

    private final Event valideEvent = new Event(1L, "event", 19.9, "place", LocalDateTime.now().plusMonths(1), 300, LocalDateTime.now(), LocalDateTime.now());

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventService eventService;

    @Test
    void createEventTest() throws BadArgumentException {
        when(eventRepository.save(any())).thenReturn(valideEvent);

        Event result = eventService.create(new CreateEventDto(valideEvent.getName(),
                valideEvent.getTicketPrice(),
                valideEvent.getAvailableTickets(),
                valideEvent.getPlace(),
                valideEvent.getDate()));

        verify(eventRepository).save(any());

        assertNotNull(result);
        assertEquals(valideEvent.getName(), result.getName());
        assertEquals(valideEvent.getPlace(), result.getPlace());
        assertEquals(valideEvent.getDate(), result.getDate());
        assertEquals(valideEvent.getTicketPrice(), result.getTicketPrice());

        log.info("createEventTest --> passed");

    }
}
