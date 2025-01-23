package com.yenicilh.chatapp.user.dto.response;

public record UserDtoResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String profilePictureUrl
) {
}
