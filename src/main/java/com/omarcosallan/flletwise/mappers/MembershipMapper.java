package com.omarcosallan.flletwise.mappers;

import com.omarcosallan.flletwise.domain.member.Member;
import com.omarcosallan.flletwise.dto.member.MembershipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MembershipMapper {
    MembershipMapper INSTANCE = Mappers.getMapper(MembershipMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "organizationId", source = "organization.id")
    MembershipDTO toMembershipDTO(Member member);
}
