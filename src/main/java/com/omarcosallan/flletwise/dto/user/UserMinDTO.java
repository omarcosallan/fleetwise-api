package com.omarcosallan.flletwise.dto.user;

import java.util.UUID;

public record UserMinDTO(UUID id, String name, String email, String avatarUrl) {
}
