package com.microservices.payment;

import com.microservices.payment.modal.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class StatusChangeEvent extends ApplicationEvent {

    @Getter
    private final Payment payment;

    public StatusChangeEvent(Object source, Payment payment) {
        super(source);
        this.payment = payment;
    }

}