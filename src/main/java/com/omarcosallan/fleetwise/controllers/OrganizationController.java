package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.member.MembershipDTO;
import com.omarcosallan.fleetwise.dto.organization.CreateOrganizationRequestDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationMinDTO;
import com.omarcosallan.fleetwise.dto.organization.UpdateOrganizationDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(value = "/{slug}/membership")
    public ResponseEntity<ResponseWrapper<MembershipDTO>> getMembership(@PathVariable("slug") String slug) {
        ResponseWrapper<MembershipDTO> result = organizationService.getMembership(slug);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{slug}")
    public ResponseEntity<ResponseWrapper<OrganizationDTO>> getOrganization(@PathVariable("slug") String slug) {
        ResponseWrapper<OrganizationDTO> result = organizationService.getOrganization(slug);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<OrganizationMinDTO>>> getOrganizations() {
        ResponseWrapper<List<OrganizationMinDTO>> result = organizationService.getOrganizations();
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/{slug}")
    public ResponseEntity<Void> updateOrganization(@PathVariable("slug") String slug, @RequestBody UpdateOrganizationDTO body) {
        organizationService.updateOrganization(slug, body);
        return ResponseEntity.noContent().build();
    }
}
