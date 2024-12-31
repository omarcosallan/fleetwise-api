package com.omarcosallan.fleetwise.dto.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleResponseDTO(UUID id,
                                 String model,
                                 String manufacturer,
                                 Integer manufacturingYear,
                                 String plate,
                                 String register,
                                 Boolean active,
                                 Boolean rented,
                                 LocalDateTime createdAt,
                                 UUID organizationId,
                                 Author author) {

    public record Author(UUID id,
                         String name,
                         String avatarUrl
    ) {
    }
}
