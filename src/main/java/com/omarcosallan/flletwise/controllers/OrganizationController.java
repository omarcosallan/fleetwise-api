package com.omarcosallan.flletwise.controllers;

import com.omarcosallan.flletwise.dto.organization.CreateOrganizationRequestDTO;
import com.omarcosallan.flletwise.mappers.ResponseWrapper;
import com.omarcosallan.flletwise.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<UUID>> createOrganization(@RequestBody CreateOrganizationRequestDTO body) {
        ResponseWrapper<UUID> organizationId = organizationService.createOrganization(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationId);
    }
}
