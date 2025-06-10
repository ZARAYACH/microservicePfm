package com.microservices.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservices.common.dtos.event.CreateEventDto;
import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.exception.NotFoundException;
import com.microservices.event.EventExceptionHandler;
import com.microservices.event.mapper.EventMapper;
import com.microservices.event.modal.Event;
import com.microservices.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventController eventController;

    private ObjectMapper objectMapper;

    private Event event;
    private EventDto eventDto;
    private CreateEventDto createEventDto;

    @BeforeEach
    void setUp() {
        event = new Event(1L, "Test Event", "concert", 20.0, "Venue", LocalDateTime.now().plusDays(1), 100, Collections.singleton(300L), LocalDateTime.now(), LocalDateTime.now());
        eventDto = new EventDto(1L, "Test Event", 20.0, "Venue", event.getDate(), 100, Collections.singleton(900L), LocalDateTime.now(), LocalDateTime.now());
        createEventDto = new CreateEventDto("Test Event", 20.0, 100, "Venue", event.getDate(), "concert", Collections.singleton(900L));
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new EventExceptionHandler())
                .build();

        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void shouldReturnListOfEvents() throws Exception {
        when(eventService.list()).thenReturn(List.of(event));
        when(eventMapper.toEventDto(anyList())).thenReturn(List.of(eventDto));

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Event"));
    }

    @Test
    void shouldReturnEventById() throws Exception {
        when(eventService.findById(1L)).thenReturn(event);
        when(eventMapper.toEventDto(event)).thenReturn(eventDto);

        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Event"));
    }

    @Test
    void shouldCreateEvent() throws Exception {
        when(eventService.create(any(CreateEventDto.class))).thenReturn(event);
        when(eventMapper.toEventDto(event)).thenReturn(eventDto);

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEventDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Event"));
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        when(eventService.findById(1L)).thenReturn(event);
        when(eventService.update(eq(event), any(CreateEventDto.class))).thenReturn(event);
        when(eventMapper.toEventDto(event)).thenReturn(eventDto);

        mockMvc.perform(put("/api/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEventDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Event"));
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        when(eventService.findById(1L)).thenReturn(event);
        doNothing().when(eventService).delete(event);

        mockMvc.perform(delete("/api/v1/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void shouldReturn404_whenEventNotFound() throws Exception {
        when(eventService.findById(99L)).thenThrow(new NotFoundException("Event not found"));

        mockMvc.perform(get("/api/v1/events/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event not found"));
    }
}
