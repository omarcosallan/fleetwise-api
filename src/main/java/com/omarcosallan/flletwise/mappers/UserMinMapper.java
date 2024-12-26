package com.omarcosallan.flletwise.mappers;

import com.omarcosallan.flletwise.domain.user.User;
import com.omarcosallan.flletwise.dto.user.UserMinDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMinMapper {
    UserMinMapper INSTANCE = Mappers.getMapper(UserMinMapper.class);

    UserMinDTO toUserMinDTO(User user);
}
