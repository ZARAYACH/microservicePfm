package com.microservices.event.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double ticketPrice; // could be a list later
    private String place;
    private LocalDateTime date;
    private Integer availableTickets;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Event(String name, Double ticketPrice, String place, LocalDateTime date, Integer availableTickets) {
        this.name = name;
        this.ticketPrice = ticketPrice;
        this.place = place;
        this.date = date;
        this.availableTickets = availableTickets;
    }
}
