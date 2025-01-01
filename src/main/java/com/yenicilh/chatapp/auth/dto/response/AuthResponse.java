package com.yenicilh.chatapp.auth.dto.response;

public record AuthResponse(
        String jwt,
        Boolean isAuth
) {
}
