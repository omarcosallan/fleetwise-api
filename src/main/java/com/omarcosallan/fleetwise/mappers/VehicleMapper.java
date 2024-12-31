package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.dto.vehicle.CreateVehicleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "model", source = "dto.model")
    @Mapping(target = "manufacturer", source = "dto.manufacturer")
    @Mapping(target = "manufacturingYear", source = "dto.manufacturingYear")
    @Mapping(target = "active", source = "dto.active")
    @Mapping(target = "rented", source = "dto.rented")
    @Mapping(target = "plate", source = "dto.plate")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "organization", source = "organization")
    Vehicle toEntity(CreateVehicleDTO dto, User owner, Organization organization);
}
