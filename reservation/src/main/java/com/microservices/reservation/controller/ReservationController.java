package com.microservices.reservation.controller;

import com.microservices.common.dtos.DeleteResponse;
import com.microservices.common.dtos.reservation.CreateReservationDto;
import com.microservices.common.dtos.reservation.ReservationDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.ServiceUnavailableException;
import com.microservices.reservation.mapper.ReservationMapper;
import com.microservices.reservation.modal.Reservation;
import com.microservices.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping
    public List<ReservationDto> listReservations() {
        return reservationMapper.toReservationDto(reservationService.list());
    }

    @GetMapping("/{id}")
    public ReservationDto findReservationById(@PathVariable String id) throws NotFoundException {
        return reservationMapper.toReservationDto(reservationService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto createReservation(@RequestBody CreateReservationDto createReservationDto, Principal principal) throws BadArgumentException, ServiceUnavailableException {
        return reservationMapper.toReservationDto(reservationService.create(createReservationDto, principal.getName()));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReservation(@PathVariable String id, @RequestBody CreateReservationDto updateReservationDto, Principal principal) throws BadArgumentException, NotFoundException {
        Reservation reservation = reservationService.findById(id);
        reservationService.update(reservation, updateReservationDto, principal.getName()); // This Does Nothing
        return ;
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteReservation(@PathVariable String id, Principal principal) throws NotFoundException {
        Reservation reservation = reservationService.findById(id);
        reservationService.delete(reservation, principal.getName());
        return new DeleteResponse(true);
    }
}
