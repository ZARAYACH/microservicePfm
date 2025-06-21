package com.microservices.payment.mapper;

import com.microservices.common.dtos.payment.PaymentDto;
import com.microservices.payment.modal.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PaymentMapper {
    PaymentDto toPaymentDto(Payment payment);

    List<PaymentDto> toPaymentDto(List<Payment> list);
}