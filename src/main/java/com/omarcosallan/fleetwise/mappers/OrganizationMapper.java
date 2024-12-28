package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    @Mapping(target = "ownerId", source = "organization.owner.id")
    OrganizationDTO toOrganizationDTO(Organization organization);
}
