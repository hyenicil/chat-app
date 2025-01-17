package com.yenicilh.chatapp.user.dto.response;

import com.yenicilh.chatapp.user.entity.enums.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record UserAuthDtoResponse(
        Map<String, Object> claims,
        String username,
        String email
) {
}
