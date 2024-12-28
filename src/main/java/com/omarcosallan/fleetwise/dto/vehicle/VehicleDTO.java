package com.omarcosallan.fleetwise.dto.vehicle;

import com.omarcosallan.fleetwise.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleDTO(UUID id,
                         String model,
                         String manufacturer,
                         String plate,
                         String register,
                         Integer manufacturingYear,
                         Boolean active,
                         Boolean rented,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         Author author) {
    public VehicleDTO(UUID id,
                      String model,
                      String manufacturer,
                      String plate,
                      String register,
                      Integer manufacturingYear,
                      Boolean active,
                      Boolean rented,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      User user) {
        this(id, model, manufacturer, plate, register, manufacturingYear, active, rented, createdAt, updatedAt, new Author(user.getId(), user.getName(), user.getAvatarUrl()));
    }

    public record Author(UUID id, String name, String avatarUrl) {}
}
