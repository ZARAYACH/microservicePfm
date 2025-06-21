package com.microservices.reservation.modal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long eventId;

    private Double price;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(unique = true, nullable = false)
    private String paymentId;
    private String paymentUrl;
    @Column(unique = true, nullable = false)
    private String orderId = UUID.randomUUID().toString();

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Reservation(Long eventId, Double price, String userEmail) {
        this.eventId = eventId;
        this.price = price;
        this.userEmail = userEmail;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public enum ReservationStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }
}
