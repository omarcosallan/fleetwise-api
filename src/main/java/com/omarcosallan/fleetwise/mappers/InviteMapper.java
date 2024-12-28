package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InviteMapper {
    InviteMapper INSTANCE = Mappers.getMapper(InviteMapper.class);

    @Mapping(target = "author", source = "invite.author")
    InviteDTO toInviteDTO(Invite invite);
}
