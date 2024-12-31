package com.omarcosallan.fleetwise.dto.vehicle;

import java.util.List;

public record GetVehicleResponse(
        List<VehicleResponseDTO> vehicles,
        int pageSize,
        int pageIndex,
        int pageCount
) {}
