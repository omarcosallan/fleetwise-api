package com.omarcosallan.fleetwise.projections;

import java.time.LocalDateTime;

public interface RecentActivityProjection {
    String getType();
    LocalDateTime getCreatedAt();
    String getOrganizationName();
    String getOwnerName();
}
