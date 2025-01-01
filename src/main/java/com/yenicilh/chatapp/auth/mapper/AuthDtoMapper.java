package com.yenicilh.chatapp.auth.mapper;

import com.yenicilh.chatapp.auth.dto.request.RegisterDtoRequest;
import com.yenicilh.chatapp.user.entity.User;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {AuthDtoMapper.class}, injectionStrategy = CONSTRUCTOR)
public interface AuthDtoMapper {

    User toEntity(RegisterDtoRequest request);


}
