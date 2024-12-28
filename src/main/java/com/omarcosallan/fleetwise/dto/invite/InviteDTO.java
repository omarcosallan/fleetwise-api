package com.omarcosallan.fleetwise.dto.invite;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record InviteDTO(UUID id,
                        Role role,
                        String email,
                        LocalDateTime createdAt,
                        OrganizationName organization,
                        Author author) {

    public record Author(UUID id,
                         String name,
                         String avatarUrl) {
    }

    public record OrganizationName(String name) {}
}
