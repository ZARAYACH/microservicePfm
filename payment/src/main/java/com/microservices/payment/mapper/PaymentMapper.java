package com.microservices.payment.mapper;
import com.microservices.dtos.PaymentRequestDto;
import com.microservices.dtos.PaymentResponseDto;
import com.microservices.payment.modal.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "success", source = "success")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now().toString())")
    Payment toEntity(PaymentRequestDto request, boolean success, String message);

    PaymentResponseDto toDto(Payment payment);
}