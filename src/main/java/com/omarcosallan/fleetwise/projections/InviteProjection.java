package com.omarcosallan.fleetwise.projections;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public interface InviteProjection {
    UUID getId();
    Role getRole();
    String getEmail();
    LocalDateTime getCreatedAt();
    String getOrganizationName();
    UUID getUserId();
    String getUserName();
    String getUserAvatarUrl();
}
