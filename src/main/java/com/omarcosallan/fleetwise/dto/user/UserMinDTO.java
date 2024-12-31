package com.omarcosallan.fleetwise.dto.user;

import java.util.UUID;

public record UserMinDTO(UUID id,
                         String name,
                         String email,
                         String avatarUrl) {
}
