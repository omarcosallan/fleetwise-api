package com.omarcosallan.fleetwise.dto.organization;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;

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
                              Role role,
                              UserMinDTO owner) {
}
