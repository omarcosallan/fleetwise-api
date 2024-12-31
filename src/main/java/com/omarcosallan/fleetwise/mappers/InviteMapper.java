package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.invite.CreateInviteDTO;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InviteMapper {
    InviteMapper INSTANCE = Mappers.getMapper(InviteMapper.class);

    @Mapping(target = "author", source = "invite.author")
    InviteDTO toInviteDTO(Invite invite);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "email", source = "invite.email")
    @Mapping(target = "role", source = "invite.role")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "organization", source = "organization")
    Invite toEntity(Organization organization, CreateInviteDTO invite, User author);
}
