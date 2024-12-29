package com.omarcosallan.fleetwise.dto.vehicle;

public record CreateVehicleDTO(String model,
                               String manufacturer,
                               Integer manufacturingYear,
                               Boolean active,
                               Boolean rented,
                               String plate,
                               String register) {
}
