package com.microservices.event.modal;

import com.microservices.common.converters.LongSetToJson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

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

    private String type;
    private Double ticketPrice; // could be a list later
    private String place;
    private LocalDateTime date;
    private Integer availableTickets;

    private String organiser;//for now this now its only the email of the organizer later we can create a user's service or add user info to the auth server

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Event(String name, Double ticketPrice, String place, LocalDateTime date, Integer availableTickets, String type) {
        this.name = name;
        this.type = type;
        this.ticketPrice = ticketPrice;
        this.place = place;
        this.date = date;
        this.availableTickets = availableTickets;
    }
}
