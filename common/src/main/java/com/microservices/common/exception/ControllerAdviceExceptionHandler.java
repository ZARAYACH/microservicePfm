package com.microservices.common.exception;

import com.microservices.common.exception.modal.ExceptionDto;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

public interface ControllerAdviceExceptionHandler {

    ResponseEntity<ExceptionDto> handleNotFound(NotFoundException ex);

    ResponseEntity<ExceptionDto> handleBadArgument(BadArgumentException ex);

    ResponseEntity<ExceptionDto> handleForbidden(AccessDeniedException ex);

    ResponseEntity<ExceptionDto> handleAuthentication(AuthenticationException ex);

    ResponseEntity<ExceptionDto> handleEntityExists(EntityExistsException ex);

    // Don't ever expose the error
    ResponseEntity<ExceptionDto> handleGeneric(Exception ex);

    default ResponseEntity<ExceptionDto> buildResponse(ExceptionDto dto) {
        return new ResponseEntity<>(dto, Objects.requireNonNull(HttpStatus.resolve(dto.getStatus())));
    }

    default ExceptionDto buildExceptionDto(Exception ex, HttpStatus status) {
        return ExceptionDto.builder()
                .message(ex.getMessage())
                .status(status.value())
                .statusDescription(status.getReasonPhrase())
                .build();
    }
}
