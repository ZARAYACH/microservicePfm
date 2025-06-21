package com.microservices.reservation.service;

import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.dtos.payment.PaymentDto;
import com.microservices.common.dtos.reservation.CreateReservationDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.ServiceUnavailableException;
import com.microservices.reservation.config.ReservationServiceProperties;
import com.microservices.reservation.http.EventClient;
import com.microservices.reservation.http.PaymentClient;
import com.microservices.reservation.modal.Reservation;
import com.microservices.reservation.repository.ReservationRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EventClient eventClient;
    private final PaymentClient paymentClient;
    private final ReservationServiceProperties reservationServiceProperties;

    private final String RESERVATION_PAYMENT_CALLBACK_URL = "/api/v1/webhook/payments/%S/callback";
    private final String PAYMENT_WEBHOOK = "/api/v1/webhooks/payments";

    public static Reservation toReservations(int quantity, EventDto eventDto, String userEmail) {
        return Reservation.builder()
                .eventId(eventDto.id())
                .price(eventDto.ticketPrice())
                .userEmail(userEmail)
                .status(Reservation.ReservationStatus.PENDING)
                .quantity(quantity)
                .orderId(UUID.randomUUID().toString())
                .build();
    }

    public List<Reservation> list() {
        return reservationRepository.findAll();
    }

    public Reservation create(CreateReservationDto createReservationDto, String userEmail) throws BadArgumentException, ServiceUnavailableException {
        validateReservationInput(createReservationDto);
        EventDto eventDto = fetchAndCheckAvailability(createReservationDto);
        Reservation reservation = toReservations(createReservationDto.quantity(), eventDto, userEmail);
        PaymentDto paymentDto = createPayment(reservation);

        reservation.setPaymentId(paymentDto.id());
        reservation.setPaymentUrl(paymentDto.paymentUrl());

        return reservationRepository.save(reservation);
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

        Integer reserved = reservationRepository.sumQuantityByEventIdAndStatus(event.id(), Reservation.ReservationStatus.CONFIRMED).orElse(0);

        if ((reserved + dto.quantity()) > event.availableTickets()) {
            throw new BadArgumentException("Not enough tickets available");
        }

        return event;
    }

    @PreAuthorize("reservation.userEmail.equals(userEmail)")
    public void update(Reservation reservation, CreateReservationDto updateReservationDto, String userEmail) {
        return;
    }

    @PreAuthorize("reservation.userEmail.equals(userEmail)")
    public void delete(@NotNull Reservation reservation, String userEmail) {
        reservationRepository.delete(reservation);
    }

    public void signalReservationStatusUpdate(Reservation reservation) throws ServiceUnavailableException, NotFoundException {
        PaymentDto paymentDto = paymentClient.findPaymentById(reservation.getPaymentId())
                .orElseThrow(() -> new NotFoundException("Payment with id #" + reservation.getPaymentId() + " Not found"));
        switch (paymentDto.status()) {
            case SUCCESS -> reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
            case PENDING -> reservation.setStatus(Reservation.ReservationStatus.PENDING);
            case CANCELLED, FAILED, EXPIRED -> reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        }
        reservationRepository.save(reservation);
    }

    public Reservation findByPaymentId(@NotNull String paymentId) {
        return reservationRepository.findByPaymentId(paymentId);
    }

    private PaymentDto createPayment(Reservation reservation) throws ServiceUnavailableException {
        return paymentClient.createPayment(new CreatePaymentDto(
                        reservation.getPrice() * reservation.getQuantity(),
                        reservation.getOrderId(),
                        reservationServiceProperties.getRootUrl() + String.format(RESERVATION_PAYMENT_CALLBACK_URL, reservation.getId()),
                        reservationServiceProperties.getRootUrl() + PAYMENT_WEBHOOK
                ))
                .orElseThrow(() -> new ServiceUnavailableException("Payment service is unavailable"));
    }
}
