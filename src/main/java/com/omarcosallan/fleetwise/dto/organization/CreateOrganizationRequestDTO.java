package com.omarcosallan.fleetwise.dto.organization;

public record CreateOrganizationRequestDTO(String name,
                                           String domain,
                                           boolean shouldAttachUsersByDomain) {
}
