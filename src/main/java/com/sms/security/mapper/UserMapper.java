package com.sms.security.mapper;

import org.mapstruct.Mapper;

import com.sms.security.dto.UserResponseDto;
import com.sms.security.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);
}
