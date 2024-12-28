package com.omarcosallan.fleetwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class MemberNotFoundException extends FleetWiseException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Member not found.");
        problemDetail.setDetail("No members were found");

        return problemDetail;
    }
}