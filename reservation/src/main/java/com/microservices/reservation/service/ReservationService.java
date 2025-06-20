package com.microservices.reservation.service;

import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.dtos.reservation.CreateReservationDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.ServiceUnavailableException;
import com.microservices.reservation.EventClient;
import com.microservices.reservation.modal.Reservation;
import com.microservices.reservation.repository.ReservationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventClient eventClient;

    public List<Reservation> list() {
        return reservationRepository.findAll();
    }

    public List<Reservation> create(CreateReservationDto createReservationDto, String userEmail) throws BadArgumentException, ServiceUnavailableException {
        validateReservationInput(createReservationDto);
        EventDto eventDto = fetchAndCheckAvailability(createReservationDto);
        List<Reservation> reservations = toReservations(createReservationDto.quantity(), eventDto, userEmail);
        return reservationRepository.saveAll(reservations);
    }


    public Reservation findById(String id) throws NotFoundException {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation with id #" + id + " Not found"));
    }

    public void validateReservationInput(CreateReservationDto dto) throws BadArgumentException {
        try {
            Assert.notNull(dto.eventId(), "Event ID is required");
            Assert.isTrue(dto.quantity() > 0 && dto.quantity() <= 100, "Quantity must be between 1 and 100");
        } catch (Exception e) {
            throw new BadArgumentException(e);
        }

    }

    private EventDto fetchAndCheckAvailability(CreateReservationDto dto) throws BadArgumentException, ServiceUnavailableException {
        EventDto event = eventClient.getEventById(dto.eventId())
                .orElseThrow(() -> new BadArgumentException("Event with id #" + dto.eventId() + " Not found"));

        Assert.isTrue(event.date().isAfter(LocalDateTime.now()), "Event has already started");

        long reserved = reservationRepository.countReservationByEventIdAndStatus(event.id(), Reservation.ReservationStatus.CONFIRMED);

        if ((reserved + dto.quantity()) > event.availableTickets()) {
            throw new BadArgumentException("Not enough tickets available");
        }

        return event;
    }


    @PreAuthorize("reservation.userEmail.equals(userEmail)")
    public void update(Reservation reservation, CreateReservationDto updateReservationDto, String userEmail) {
        return ;
    }

    @PreAuthorize("reservation.userEmail.equals(userEmail)")
    public void delete(@NotNull Reservation reservation, String userEmail) {
        reservationRepository.delete(reservation);
    }

    public static List<Reservation> toReservations(int quantity, EventDto eventDto, String userEmail) {
        List<Reservation> list = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            list.add(Reservation.builder()
                    .eventId(eventDto.id())
                    .price(eventDto.ticketPrice())
                    .userEmail(userEmail)
                    .status(Reservation.ReservationStatus.PENDING)
                    .build());
        }
        return list;
    }
}
