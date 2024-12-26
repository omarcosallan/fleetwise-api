package com.omarcosallan.fleetwise.dto.organization;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationDTO(UUID id,
                              String name,
                              String slug,
                              String domain,
                              Boolean shouldAttachUsersByDomain,
                              String avatarUrl,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              UUID ownerId) {
}
