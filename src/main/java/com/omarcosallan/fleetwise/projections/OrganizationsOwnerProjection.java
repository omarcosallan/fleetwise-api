package com.omarcosallan.fleetwise.projections;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OrganizationsOwnerProjection {
    UUID getId();
    String getName();
    String getSlug();
    String getDomain();
    Boolean getShouldAttachUsersByDomain();
    String getAvatarUrl();
    LocalDateTime getCreatedAt();
    Role getRole();
    UUID getOwnerId();
    String getOwnerName();
    String getOwnerEmail();
    String getOwnerAvatarUrl();
}
