package com.microservices.event.controller;

import com.microservices.common.dtos.DeleteResponse;
import com.microservices.common.dtos.event.CreateEventDto;
import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.event.mapper.EventMapper;
import com.microservices.event.modal.Event;
import com.microservices.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public List<EventDto> listEvents() {
        return eventMapper.toEventDto(eventService.list());
    }

    @GetMapping("/{id}")
    public EventDto findEventById(@PathVariable Long id) throws NotFoundException {
        return eventMapper.toEventDto(eventService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_EVENT_ORGANISER')")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody CreateEventDto createEventDto) throws BadArgumentException {
        return eventMapper.toEventDto(eventService.create(createEventDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EVENT_ORGANISER')")
    public EventDto updateEvent(@PathVariable Long id, @RequestBody CreateEventDto updateEventDto) throws BadArgumentException, NotFoundException {
        Event event = eventService.findById(id);
        return eventMapper.toEventDto(eventService.update(event, updateEventDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_EVENT_ORGANISER')")
    public DeleteResponse deleteEvent(@PathVariable Long id) throws NotFoundException {
        Event event = eventService.findById(id);
        eventService.delete(event);
        return new DeleteResponse(true);
    }
}
