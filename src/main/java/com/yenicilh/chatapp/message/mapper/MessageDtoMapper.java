package com.yenicilh.chatapp.message.mapper;

import com.yenicilh.chatapp.chat.mapper.ChatDtoMapper;
import com.yenicilh.chatapp.message.dto.MessageDtoRequest;
import com.yenicilh.chatapp.message.entity.Message;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

//@Mapper(componentModel = SPRING, uses = {MessageDtoMapper.class}, injectionStrategy = CONSTRUCTOR)
public interface MessageDtoMapper {
    Message toEntity(MessageDtoRequest request);
}
