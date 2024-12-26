package com.omarcosallan.flletwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class FleetWiseException extends RuntimeException {

    public FleetWiseException() {}

    public FleetWiseException(String message) {
        super(message);
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pb.setTitle("FleetWise internal server error");
        return pb;
    }
}
