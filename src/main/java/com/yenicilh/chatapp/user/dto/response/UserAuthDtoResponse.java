package com.yenicilh.chatapp.user.dto.response;

import com.yenicilh.chatapp.user.entity.enums.Role;

import java.util.Map;
import java.util.Set;

public record UserAuthDtoResponse(
        Set<Role> authorities,
        String username,
        String email
) {
}
