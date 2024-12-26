package com.omarcosallan.fleetwise.dto.organization;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.util.UUID;

public record OrganizationMinDTO(UUID id,
                                 String name,
                                 String slug,
                                 String avatarUrl,
                                 Role role) {
}
