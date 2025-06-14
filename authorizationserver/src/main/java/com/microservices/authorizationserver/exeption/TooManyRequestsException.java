package com.microservices.authorizationserver.exeption;

import lombok.experimental.StandardException;

@StandardException
public class TooManyRequestsException extends Exception {
}
