package com.microservices.reservation.repository;

import com.microservices.reservation.modal.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT SUM(r.quantity) FROM Reservation r WHERE r.eventId = :eventId AND r.status = :status")
    Optional<Integer> sumQuantityByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") Reservation.ReservationStatus status);

    Reservation findByPaymentId(String paymentId);
}
