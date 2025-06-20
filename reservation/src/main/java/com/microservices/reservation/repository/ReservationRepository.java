package com.microservices.reservation.repository;

import com.microservices.reservation.modal.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    int countReservationByEventIdAndStatus(Long eventId, Reservation.ReservationStatus status);
}
