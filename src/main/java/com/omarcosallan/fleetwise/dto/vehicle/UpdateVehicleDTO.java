package com.omarcosallan.fleetwise.dto.vehicle;

public record UpdateVehicleDTO(String model,
                               String manufacturer,
                               Integer manufacturingYear,
                               Boolean active,
                               Boolean rented) {
}
