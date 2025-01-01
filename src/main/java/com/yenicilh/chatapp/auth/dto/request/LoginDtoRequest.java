package com.yenicilh.chatapp.auth.dto.request;

public record LoginDtoRequest(
        String email,
        String password
) {
}
