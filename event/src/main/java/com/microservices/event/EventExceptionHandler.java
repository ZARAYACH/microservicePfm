package com.microservices.event;

import com.microservices.common.exception.BadArgumentException;
import com.microservices.common.exception.ControllerAdviceExceptionHandler;
import com.microservices.common.exception.NotFoundException;
import com.microservices.common.exception.modal.ExceptionDto;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j

public class EventExceptionHandler implements ControllerAdviceExceptionHandler {

    @Override
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleNotFound(NotFoundException ex) {
        ExceptionDto dto = buildExceptionDto(ex, HttpStatus.NOT_FOUND);
        log.debug("Exception #{}", dto.getErrorId(), ex);
        return buildResponse(dto);
    }

    @Override
    @ExceptionHandler(BadArgumentException.class)
    public ResponseEntity<ExceptionDto> handleBadArgument(BadArgumentException ex) {
        ExceptionDto dto = buildExceptionDto(ex, HttpStatus.BAD_REQUEST);
        log.debug("Exception #{}", dto.getErrorId(), ex);
        return buildResponse(dto);
    }

    @Override
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> handleForbidden(AccessDeniedException ex) {
        ExceptionDto dto = buildExceptionDto(ex, HttpStatus.FORBIDDEN);
        log.debug("Exception #{}", dto.getErrorId(), ex);
        return buildResponse(dto);
    }

    @Override
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDto> handleAuthentication(AuthenticationException ex) {
        ExceptionDto dto = buildExceptionDto(ex, HttpStatus.UNAUTHORIZED);
        log.debug("Exception #{}", dto.getErrorId(), ex);
        return buildResponse(dto);

    }

    @Override
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionDto> handleEntityExists(EntityExistsException ex) {
        ExceptionDto dto = buildExceptionDto(ex, HttpStatus.CONFLICT);
        log.debug("Exception #{}", dto.getErrorId(), ex);
        return buildResponse(dto);

    }

    @Override
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGeneric(Exception ex) {
        ExceptionDto dto = buildExceptionDto(new Exception("INTERNAL SERVER ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Exception #{}", dto.getErrorId(), ex);
        return buildResponse(dto);
    }

}
