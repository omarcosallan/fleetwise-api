package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.organization.CreateOrganizationRequestDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "ownerId", source = "organization.owner.id")
    OrganizationDTO toOrganizationDTO(Organization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "invites", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "domain", source = "dto.domain")
    @Mapping(target = "shouldAttachUsersByDomain", source = "dto.shouldAttachUsersByDomain")
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "owner", source = "owner")
    Organization toEntity(CreateOrganizationRequestDTO dto, String slug, User owner);
}
