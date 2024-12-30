package com.omarcosallan.fleetwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class VehicleNotFoundException extends FleetWiseException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Vehicle not found.");
        problemDetail.setDetail("No vehicles were found");

        return problemDetail;
    }
}