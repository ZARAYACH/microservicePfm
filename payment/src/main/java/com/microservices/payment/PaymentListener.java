package com.microservices.payment;


import com.microservices.payment.config.ApplicationContextProvider;
import com.microservices.payment.modal.Payment;
import jakarta.persistence.PostUpdate;


public class PaymentListener {


    @PostUpdate
    public void postUpdate(Payment payment) {
        ApplicationContextProvider.getApplicationContext().publishEvent(new StatusChangeEvent(this, payment));
    }

}