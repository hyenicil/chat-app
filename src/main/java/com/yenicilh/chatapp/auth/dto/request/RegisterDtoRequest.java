package com.yenicilh.chatapp.auth.dto.request;

public record RegisterDtoRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password
){
}
