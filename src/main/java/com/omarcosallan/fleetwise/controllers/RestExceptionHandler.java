package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.exceptions.FleetWiseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("You're not allowed to perform this action.");
        return problemDetail;
    }

    @ExceptionHandler(FleetWiseException.class)
    public ProblemDetail handleSaaSException(FleetWiseException e) {
        return e.toProblemDetail();
    }
}
