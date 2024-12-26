package com.omarcosallan.fleetwise.dto.member;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.util.UUID;

public record MembershipDTO(UUID id,
                            Role role,
                            UUID userId,
                            UUID organizationId) {
}
