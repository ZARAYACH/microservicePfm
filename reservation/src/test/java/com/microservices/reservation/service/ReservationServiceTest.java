package com.microservices.reservation.service;

import com.microservices.common.PaymentStatus;
import com.microservices.common.dtos.event.EventDto;
import com.microservices.common.dtos.payment.CreatePaymentDto;
import com.microservices.common.dtos.payment.PaymentDto;
import com.microservices.common.dtos.reservation.CreateReservationDto;
import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.NotFoundException;
import com.microservices.reservation.config.ReservationServiceProperties;
import com.microservices.reservation.http.EventClient;
import com.microservices.reservation.http.PaymentClient;
import com.microservices.reservation.modal.Reservation;
import com.microservices.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private EventClient eventClient;
    @Mock
    private PaymentClient paymentClient;
    @Mock
    private ReservationServiceProperties reservationServiceProperties;

    @InjectMocks
    private ReservationService reservationService;

    private EventDto eventDto;
    private CreateReservationDto createReservationDto;
    private PaymentDto paymentDto;
    private Reservation reservation;

    @BeforeEach
    void setup() {
        eventDto = new EventDto(
                1L, "Concert", 100.0, "Venue",
                LocalDateTime.now().plusDays(1), 100,
                "organiser@email.com", LocalDateTime.now(), LocalDateTime.now()
        );

        createReservationDto = new CreateReservationDto(1L, 1);

        paymentDto = new PaymentDto("1", "", 100.0, PaymentStatus.SUCCESS, LocalDateTime.now(), LocalDateTime.now(), "http://payment.url", "http://payment.url/webhook");

        reservation = Reservation.builder()
                .id("1")
                .eventId(1L)
                .price(100.0)
                .userEmail("test@email.com")
                .status(Reservation.ReservationStatus.PENDING)
                .quantity(1)
                .orderId(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void testCreateReservation_success() throws Exception {
        when(eventClient.getEventById(1L)).thenReturn(Optional.of(eventDto));
        when(reservationRepository.sumQuantityByEventIdAndStatus(eq(1L), any())).thenReturn(Optional.of(0));
        when(paymentClient.createPayment(any(CreatePaymentDto.class))).thenReturn(Optional.of(paymentDto));
        when(reservationServiceProperties.getRootUrl()).thenReturn("http://localhost");
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation saved = reservationService.create(createReservationDto, "test@email.com");

        assertNotNull(saved);
        assertEquals("test@email.com", saved.getUserEmail());
        verify(eventClient).getEventById(1L);
        verify(paymentClient).createPayment(any(CreatePaymentDto.class));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_throwsBadArgument_onInvalidQuantity() {
        CreateReservationDto invalidDto = new CreateReservationDto(1L, 0);
        assertThrows(BadArgumentException.class, () -> reservationService.create(invalidDto, "user@example.com"));
    }

    @Test
    void testFindById_success() throws NotFoundException {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        Reservation found = reservationService.findById("1");
        assertEquals("1", found.getId());
    }

    @Test
    void testFindById_notFound() {
        when(reservationRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reservationService.findById("1"));
    }

    @Test
    void testSignalReservationStatusUpdate_success() throws Exception {
        Reservation confirmed = new Reservation();
        confirmed.setPaymentId("1");
        confirmed.setStatus(Reservation.ReservationStatus.PENDING);
        when(paymentClient.findPaymentById("1")).thenReturn(Optional.of(paymentDto));

        reservationService.signalReservationStatusUpdate(confirmed);

        assertEquals(Reservation.ReservationStatus.CONFIRMED, confirmed.getStatus());
    }

    @Test
    void testFindByPaymentId() {
        when(reservationRepository.findByPaymentId("pid")).thenReturn(reservation);
        Reservation found = reservationService.findByPaymentId("pid");
        assertEquals(reservation.getId(), found.getId());
    }
}
