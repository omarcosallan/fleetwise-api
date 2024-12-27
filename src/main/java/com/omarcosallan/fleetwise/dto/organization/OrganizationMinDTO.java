package com.omarcosallan.fleetwise.dto.organization;

import java.util.UUID;

public record OrganizationMinDTO(UUID id,
                                 String name,
                                 String slug,
                                 String domain) {
}
