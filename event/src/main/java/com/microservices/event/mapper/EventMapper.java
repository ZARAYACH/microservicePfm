package com.microservices.event.mapper;

import com.microservices.common.dtos.event.EventDto;
import com.microservices.event.modal.Event;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EventMapper {

    EventDto toEventDto(Event event);

    List<EventDto> toEventDto(List<Event> events);
}
