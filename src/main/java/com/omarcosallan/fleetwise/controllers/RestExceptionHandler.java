package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.exceptions.FleetWiseException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(FleetWiseException.class)
    public ProblemDetail handleSaaSException(FleetWiseException e) {
        return e.toProblemDetail();
    }
}
