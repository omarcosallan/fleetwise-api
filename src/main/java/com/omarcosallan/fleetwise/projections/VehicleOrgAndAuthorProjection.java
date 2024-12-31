package com.omarcosallan.fleetwise.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface VehicleOrgAndAuthorProjection {
    UUID getId();
    String getModel();
    String getManufacturer();
    Integer getManufacturingYear();
    String getPlate();
    String getRegister();
    Boolean getActive();
    Boolean getRented();
    LocalDateTime getCreatedAt();
    UUID getOrganizationId();
    UUID getAuthorId();
    String getAuthorName();
    String getAuthorAvatarUrl();
}
