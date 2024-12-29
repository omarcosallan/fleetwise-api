package com.omarcosallan.fleetwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class VehicleAlreadyExistsException extends FleetWiseException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Vehicle with same license plate or registration already exists.");

        return problemDetail;
    }
}