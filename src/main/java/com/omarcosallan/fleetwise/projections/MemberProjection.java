package com.omarcosallan.fleetwise.projections;

import com.omarcosallan.fleetwise.domain.enums.Role;

import java.util.UUID;

public interface MemberProjection {
    UUID getId();
    UUID getUserId();
    Role getRole();
    String getUserName();
    String getUserEmail();
    String getUserAvatarUrl();
}
