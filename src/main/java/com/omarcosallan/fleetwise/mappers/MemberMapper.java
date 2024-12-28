package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.dto.member.MemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    MemberDTO toMemberDTO(Member member);
}
