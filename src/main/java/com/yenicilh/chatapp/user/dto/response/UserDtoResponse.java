package com.yenicilh.chatapp.user.dto.response;

public record UserDtoResponse(
    String firstName,
    String lastName,
    String username,
    String email,
    String profilePictureUrl
) {
}
