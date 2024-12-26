package com.omarcosallan.flletwise.mappers;

import com.omarcosallan.flletwise.domain.organization.Organization;
import com.omarcosallan.flletwise.dto.organization.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    @Mapping(target = "ownerId", source = "owner.id")
    OrganizationDTO toOrganizationDTO(Organization organization);
}
