package com.yenicilh.chatapp.chat.mapper;


import com.yenicilh.chatapp.chat.dto.request.GroupChatDtoRequest;
import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.user.mapper.UserDtoMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

//@Mapper(componentModel = SPRING, uses = {ChatDtoMapper.class, UserDtoMapper.class}, injectionStrategy = CONSTRUCTOR)
public interface ChatDtoMapper
{
    Chat toEntity(GroupChatDtoRequest groupChatDtoRequest);
}
