package com.omarcosallan.fleetwise.dto.organization;

public record UpdateOrganizationDTO(String name,
                                    String domain,
                                    Boolean shouldAttachUsersByDomain) {
}
