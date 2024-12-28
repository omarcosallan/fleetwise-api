package com.omarcosallan.fleetwise.dto.invite;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record InviteDTO(UUID id,
                        Role role,
                        String email,
                        LocalDateTime createdAt,
                        Author author) {

    public record Author(UUID id,
                         String name,
                         String avatarUrl) {
        public Author(User author) {
            this(author.getId(), author.getName(), author.getAvatarUrl());
        }
    }
}
