package com.omarcosallan.flletwise.dto.member;

import com.omarcosallan.flletwise.domain.enums.Role;

import java.util.UUID;

public record MembershipDTO(UUID id,
                            Role role,
                            UUID userId,
                            UUID organizationId) {
}
