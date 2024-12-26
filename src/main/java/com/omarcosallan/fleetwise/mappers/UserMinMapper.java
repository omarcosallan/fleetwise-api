package com.omarcosallan.fleetwise.mappers;

import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMinMapper {
    UserMinMapper INSTANCE = Mappers.getMapper(UserMinMapper.class);

    UserMinDTO toUserMinDTO(User user);
}
