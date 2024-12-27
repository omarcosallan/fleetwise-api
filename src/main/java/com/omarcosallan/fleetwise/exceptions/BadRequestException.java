package com.omarcosallan.fleetwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BadRequestException extends FleetWiseException {

    private String message;

    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle(message);

        return problemDetail;
    }
}
