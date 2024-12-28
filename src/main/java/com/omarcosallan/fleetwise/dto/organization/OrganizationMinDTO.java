package com.omarcosallan.fleetwise.dto.organization;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationMinDTO(UUID id, String name, String slug, String avatarUrl, LocalDateTime createdAt,
                                 Role role, UserMinDTO owner) {
    public OrganizationMinDTO(UUID id, String name, String slug, String avatarUrl, LocalDateTime createdAt, Role role, User owner) {
        this(id, name, slug, avatarUrl, createdAt, role, new UserMinDTO(owner.getId(), owner.getName(), owner.getEmail(), owner.getAvatarUrl()));
    }
}
