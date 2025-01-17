package com.yenicilh.chatapp.auth.service.impl;

import com.yenicilh.chatapp.auth.dto.request.LoginDtoRequest;
import com.yenicilh.chatapp.auth.dto.request.RegisterDtoRequest;
import com.yenicilh.chatapp.auth.mapper.AuthDtoMapper;
import com.yenicilh.chatapp.auth.service.AuthService;
import com.yenicilh.chatapp.common.security.jwt.JwtService;
import com.yenicilh.chatapp.user.dto.response.UserAuthDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImplementation implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthDtoMapper authDtoMapper;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImplementation(JwtService jwtService, UserService userService, AuthDtoMapper authDtoMapper, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authDtoMapper = authDtoMapper;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public String register(RegisterDtoRequest request) {
        UserAuthDtoResponse response = userService.save(authDtoMapper.toEntity(request));
        return generateToken(response.authorities(), response.username());
    }

    @Override
    public String login(LoginDtoRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            User user = (User) authentication.getPrincipal();
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getAuthorities().stream().findFirst().orElse(null));

            return generateToken(claims, user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }


    private String generateToken(Map<String, Object> authorities, String username) {
        return jwtService.generateToken(authorities, username);
    }

}
