package com.yenicilh.chatapp.message.dto;

public record MessageDtoRequest(
        Long user,
        Long chat,
        String content
) {
}
