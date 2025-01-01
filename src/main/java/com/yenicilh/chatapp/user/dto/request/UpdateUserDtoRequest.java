package com.yenicilh.chatapp.user.dto.request;

public record UpdateUserDtoRequest(

        String firstName,
        String lastName,
        String username,
        String email,
        String phoneNumber,
        String profilePictureUrl

) {

}
