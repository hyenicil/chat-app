package com.yenicilh.chatapp.auth.controller;

import com.yenicilh.chatapp.auth.dto.request.LoginDtoRequest;
import com.yenicilh.chatapp.auth.dto.request.RegisterDtoRequest;
import com.yenicilh.chatapp.auth.service.AuthService;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.auth.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterDtoRequest request) throws UserException {
        String jwt  = authService.register(request);
        return new ResponseEntity<AuthResponse>(new AuthResponse(jwt, true), HttpStatus.CREATED);
    }

    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDtoRequest request) throws UserException {
        String jwt  = authService.login(request);
        return new ResponseEntity<AuthResponse>(new AuthResponse(jwt, true), HttpStatus.ACCEPTED);
    }
}
