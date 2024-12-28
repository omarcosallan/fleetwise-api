package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VehicleMapper {
    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    @Mapping(target = "author", source = "vehicle.createdBy")
    @Mapping(target = "organizationId", source = "vehicle.owner.id")
    VehicleDTO toVehicleDTO(Vehicle vehicle);
}
