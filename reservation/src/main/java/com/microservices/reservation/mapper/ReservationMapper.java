package com.microservices.reservation.mapper;

import com.microservices.common.dtos.reservation.ReservationDto;
import com.microservices.reservation.modal.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ReservationMapper {

    ReservationDto toReservationDto(Reservation reservation);

    List<ReservationDto> toReservationDto(List<Reservation> reservations);
}
