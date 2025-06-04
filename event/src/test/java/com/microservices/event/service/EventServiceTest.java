package com.microservices.event.service;

import com.microservices.common.dtos.event.CreateEventDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.event.EventRepository;
import com.microservices.event.modal.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class EventServiceTest {

    private Event validEvent;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventService eventService;

    @BeforeEach
    void setUp() {
        validEvent = new Event(
                1L,
                "Event Name",
                25.0,
                "Test Place",
                LocalDateTime.now().plusDays(1),
                100,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }


    @Test
    void createEventTest() throws BadArgumentException {
        when(eventRepository.save(any())).thenReturn(validEvent);

        Event result = eventService.create(new CreateEventDto(validEvent.getName(),
                validEvent.getTicketPrice(),
                validEvent.getAvailableTickets(),
                validEvent.getPlace(),
                validEvent.getDate()));

        verify(eventRepository).save(any());

        assertNotNull(result);
        assertEquals(validEvent.getName(), result.getName());
        assertEquals(validEvent.getPlace(), result.getPlace());
        assertEquals(validEvent.getDate(), result.getDate());
        assertEquals(validEvent.getTicketPrice(), result.getTicketPrice());

        log.info("createEventTest --> passed");
    }

    @Test
    void deleteEventTest() {
        eventService.delete(validEvent);
        verify(eventRepository).delete(validEvent);
    }

    @Test
    void findById_shouldReturnEvent_whenExists() throws NotFoundException {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(validEvent));

        Event result = eventService.findById(1L);

        assertEquals(validEvent, result);
        verify(eventRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowNotFoundException_whenNotExists() {
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.findById(2L));
    }

    @Test
    void update_shouldUpdateEventSuccessfully() throws BadArgumentException {
        CreateEventDto updateDto = new CreateEventDto(
                "Updated Event",
                30.0,
                80,
                "Updated Place",
                LocalDateTime.now().plusDays(5)
        );

        when(eventRepository.save(any(Event.class))).thenReturn(validEvent);

        Event updated = eventService.update(validEvent, updateDto);

        assertNotNull(updated);
        verify(eventRepository).save(any(Event.class));
        assertEquals("Updated Event", validEvent.getName());
        assertEquals("Updated Place", validEvent.getPlace());
    }

    @Test
    void update_shouldThrowBadArgumentException_onInvalidDto() {
        CreateEventDto invalidDto = new CreateEventDto(
                "",
                -1.0,
                0,
                "",
                LocalDateTime.now().minusDays(1)
        );

        assertThrows(BadArgumentException.class, () -> eventService.update(validEvent, invalidDto));
    }
}
