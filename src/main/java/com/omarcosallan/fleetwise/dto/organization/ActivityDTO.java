package com.omarcosallan.fleetwise.dto.organization;

import java.time.LocalDateTime;

public record ActivityDTO(String type,
                          LocalDateTime createdAt,
                          String ownerName,
                          String organizationName) {
}
