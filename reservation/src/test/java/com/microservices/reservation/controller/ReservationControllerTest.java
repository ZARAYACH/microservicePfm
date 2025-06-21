package com.microservices.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.common.dtos.reservation.CreateReservationDto;
import com.microservices.common.dtos.reservation.ReservationDto;
import com.microservices.common.exception.NotFoundException;
import com.microservices.reservation.ReservationExceptionHandler;
import com.microservices.reservation.mapper.ReservationMapper;
import com.microservices.reservation.modal.Reservation;
import com.microservices.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Principal mockPrincipal = () -> "test@example.com";
    private MockMvc mockMvc;
    @Mock
    private ReservationService reservationService;
    @Mock
    private ReservationMapper reservationMapper;
    @InjectMocks
    private ReservationController reservationController;
    private Reservation reservation;
    private ReservationDto reservationDto;
    private CreateReservationDto createReservationDto;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
                .setControllerAdvice(new ReservationExceptionHandler())
                .build();


        reservation = Reservation.builder()
                .id("1")
                .eventId(1L)
                .price(100.0)
                .userEmail("test@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        reservationDto = new ReservationDto("1", 1L, "user@email.com", Reservation.ReservationStatus.PENDING.toString(), "44", "http://payment.url", LocalDateTime.now(), LocalDateTime.now());
        createReservationDto = new CreateReservationDto(1L, 1);
    }

    @Test
    void shouldListReservations() throws Exception {
        when(reservationService.list()).thenReturn(List.of(reservation));
        when(reservationMapper.toReservationDto(anyList())).thenReturn(List.of(reservationDto));

        mockMvc.perform(get("/api/v1/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldFindReservationById() throws Exception {
        when(reservationService.findById("1")).thenReturn(reservation);
        when(reservationMapper.toReservationDto(any(Reservation.class))).thenReturn(reservationDto);

        mockMvc.perform(get("/api/v1/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void shouldCreateReservation() throws Exception {
        when(reservationService.create(createReservationDto, "user@email.com")).thenReturn(reservation);
        when(reservationMapper.toReservationDto(any(Reservation.class))).thenReturn(reservationDto);

        mockMvc.perform(post("/api/v1/reservations")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReservationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateReservation() throws Exception {
        when(reservationService.findById("1")).thenReturn(reservation);
        doNothing().when(reservationService).update(any(), any(), anyString());

        mockMvc.perform(put("/api/v1/reservations/1")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReservationDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        when(reservationService.findById("1")).thenReturn(reservation);
        doNothing().when(reservationService).delete(any(), anyString());

        mockMvc.perform(delete("/api/v1/reservations/1")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted").value(true));
    }

    @Test
    void shouldReturnNotFoundWhenReservationMissing() throws Exception {
        when(reservationService.findById("99"))
                .thenThrow(new NotFoundException("Reservation not found"));

        mockMvc.perform(get("/api/v1/reservations/99"))
                .andExpect(status().isNotFound());
    }
}
