package com.project.airbnb.mapper;

import com.project.airbnb.dto.response.UserResponse;
import com.project.airbnb.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
