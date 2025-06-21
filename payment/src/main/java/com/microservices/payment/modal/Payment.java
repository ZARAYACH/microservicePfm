package com.microservices.payment.modal;

import com.microservices.common.PaymentStatus;
import com.microservices.payment.PaymentListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@EntityListeners(PaymentListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private String id; //Should be an uuid
    private String cardHolder;
    private String email;

    @Column(nullable = false)
    private double amount;
    @Column(nullable = false)
    private String orderId;
    @Column(nullable = false)
    private String callbackUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    @Column(nullable = false)
    private String paymentUrl;
    private String webHookUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}