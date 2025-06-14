package com.microservices.authorizationserver.exeption;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class AuthenticationInvalidSessionException extends AuthenticationException {

}
