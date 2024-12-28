package com.omarcosallan.fleetwise.dto.member;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.util.UUID;

public record MemberDTO(UUID id,
                        UUID userId,
                        Role role,
                        String name,
                        String email,
                        String avatarUrl) {
}
