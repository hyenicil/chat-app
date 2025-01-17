package com.yenicilh.chatapp.auth.service;

import com.yenicilh.chatapp.auth.dto.request.LoginDtoRequest;
import com.yenicilh.chatapp.auth.dto.request.RegisterDtoRequest;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.user.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AuthService {

    String register(RegisterDtoRequest request) throws UserException;

    String login(LoginDtoRequest request) throws UserException;

}
