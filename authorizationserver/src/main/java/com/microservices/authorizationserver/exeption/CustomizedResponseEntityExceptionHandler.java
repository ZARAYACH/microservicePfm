package com.microservices.authorizationserver.exeption;

import jakarta.annotation.Priority;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import com.microservices.authorizationserver.exeption.modal.ExceptionDto;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@RestControllerAdvice
@Priority(Ordered.HIGHEST_PRECEDENCE)
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDto> handleNotFoundException(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDto> handleBadRequestExceptions(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class, AccessDeniedException.class, ExpiredException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionDto> handleUnauthorizedException(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionDto> handleUnauthenticatedException(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({TooManyRequestsException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<ExceptionDto> handleTooManyRequestsException(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionDto> handleEntityAlreadyExists(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({DistanceCalculationException.class,
            QuoteCalculationServiceException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ExceptionDto> handleDistanceCalculationException(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> handleAnyOtherException(Exception ex) {
        ExceptionDto exceptionDto = new ExceptionDto(List.of(ex.getMessage()));
        log.error("Error id: {}", exceptionDto.getErrorId(), ex);
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}



