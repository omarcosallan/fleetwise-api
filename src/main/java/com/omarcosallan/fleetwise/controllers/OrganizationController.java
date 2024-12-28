package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.member.MembershipDTO;
import com.omarcosallan.fleetwise.dto.organization.*;
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
    public ResponseEntity<MembershipDTO> getMembership(@PathVariable("slug") String slug) {
        MembershipDTO result = organizationService.getMembership(slug);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{slug}")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable("slug") String slug) {
        OrganizationDTO result = organizationService.getOrganization(slug);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<OrganizationWithOwnerDTO>> getOrganizations() {
        List<OrganizationWithOwnerDTO> result = organizationService.getOrganizations();
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/{slug}")
    public ResponseEntity<Void> updateOrganization(@PathVariable("slug") String slug, @RequestBody UpdateOrganizationDTO body) {
        organizationService.updateOrganization(slug, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{slug}")
    public ResponseEntity<Void> shutdownOrganization(@PathVariable("slug") String slug) {
        organizationService.shutdownOrganization(slug);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{slug}/owner")
    public ResponseEntity<Void> transferOrganization(@PathVariable("slug") String slug, @RequestBody TransferOrganizationRequestDTO body) {
        organizationService.transferOrganization(slug, body);
        return ResponseEntity.noContent().build();
    }
}
