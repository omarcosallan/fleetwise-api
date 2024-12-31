package com.omarcosallan.fleetwise.dto.organization;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationWithOwnerDTO(UUID id,
                                       String name,
                                       String slug,
                                       String domain,
                                       Boolean shouldAttachUsersByDomain,
                                       String avatarUrl,
                                       LocalDateTime createdAt,
                                       Role role,
                                       UserMinDTO owner) {
}
