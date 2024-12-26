package com.omarcosallan.flletwise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class OrganizationDomainAlreadyExistsException extends FleetWiseException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Another organization with same domain already exists.");

        return problemDetail;
    }
}
