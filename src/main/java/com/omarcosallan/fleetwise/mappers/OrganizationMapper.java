package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import com.omarcosallan.fleetwise.projections.OrganizationProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    @Mapping(target = "owner", expression = "java(mapToUserMinDTO(projection.getOwnerId(), projection.getOwnerName(), projection.getOwnerEmail(), projection.getOwnerAvatarUrl()))")
    OrganizationDTO toOrganizationDTO(OrganizationProjection projection);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "owner", expression = "java(mapToUserMinDTO(organization.getOwner().getId(), organization.getOwner().getName(), organization.getOwner().getEmail(), organization.getOwner().getAvatarUrl()))")
    OrganizationDTO toOrganizationDTO(Organization organization, Role role);

    default UserMinDTO mapToUserMinDTO(UUID id, String name, String email, String avatarUrl) {
        return new UserMinDTO(id, name, email, avatarUrl);
    }
}
