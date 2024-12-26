package com.omarcosallan.flletwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UnauthorizedException extends FleetWiseException {
    private String message;

    public UnauthorizedException(String message) {
        this.message = message;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problemDetail.setTitle(message);

        return problemDetail;
    }
}
