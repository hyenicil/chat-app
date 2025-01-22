package com.yenicilh.chatapp.user.mapper;


import com.yenicilh.chatapp.user.dto.request.UserDtoRequest;
import com.yenicilh.chatapp.user.dto.response.UserDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {UserDtoMapper.class}, injectionStrategy = CONSTRUCTOR)
public interface UserDtoMapper {

    User toEntity(UserDtoRequest request);

    UserDtoResponse toResponse(User entity);

    List<UserDtoResponse> toResponseList(List<User> users);
}
