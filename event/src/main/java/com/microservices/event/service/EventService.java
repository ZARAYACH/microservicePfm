package com.microservices.event.service;

import com.microservices.common.dtos.event.CreateEventDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.event.modal.Event;
import com.microservices.event.repository.EventRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> list() {
        return eventRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_EVENT_ORGANISER')")
    public Event create(CreateEventDto createEventDto, String organiser) throws BadArgumentException {
        Event event = validateCreateEventDtoAndCreate(createEventDto);
        event.setOrganiser(organiser);
        return eventRepository.save(event);
    }

    public Event findById(long id) throws NotFoundException {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id #" + id + " Not found"));
    }

    private Event validateCreateEventDtoAndCreate(CreateEventDto createEventDto) throws BadArgumentException {
        try {
            Assert.isTrue(StringUtils.isNotBlank(createEventDto.name()), "Event Name can't be null or empty");
            Assert.isTrue(StringUtils.isNotBlank(createEventDto.type()), "Event type can't be null or empty");
            Assert.isTrue(createEventDto.date() != null && createEventDto.date().isAfter(LocalDateTime.now()), "Event Date can't be null & can't be past today");
            Assert.isTrue(StringUtils.isNotBlank(createEventDto.place()), "Event place can't be null or empty");
            Assert.isTrue(createEventDto.ticketPrice() > 0, "Event tickets price should be bigger than 0");
            Assert.isTrue(createEventDto.availableTickets() > 0, "Event available tickets should be bigger than 0");
            // Assert that organisers exists
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }
        return new Event(createEventDto.name(),
                createEventDto.ticketPrice(),
                createEventDto.place(),
                createEventDto.date(),
                createEventDto.availableTickets(),
                createEventDto.type());
    }

    @PreAuthorize("hasAuthority('ROLE_EVENT_ORGANISER') && event.organiser.equals(organiser)")
    public Event update(Event event, CreateEventDto updateEventDto, String organiser) throws BadArgumentException {
        Event newEvent = validateCreateEventDtoAndCreate(updateEventDto);

        event.setName(newEvent.getName());
        event.setDate(newEvent.getDate());
        event.setPlace(newEvent.getPlace());
        event.setTicketPrice(newEvent.getTicketPrice());
        event.setAvailableTickets(newEvent.getAvailableTickets());
        event.setType(newEvent.getType());
        event.setOrganiser(organiser);

        return eventRepository.save(event);
    }

    @PreAuthorize("hasAuthority('ROLE_EVENT_ORGANISER') && event.organiser.equals(organiser)")
    public void delete(@NotNull Event event) {
        eventRepository.delete(event);
    }
}
