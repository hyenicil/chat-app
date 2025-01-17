package com.yenicilh.chatapp.auth.controller;

import com.yenicilh.chatapp.auth.dto.request.LoginDtoRequest;
import com.yenicilh.chatapp.auth.dto.request.RegisterDtoRequest;
import com.yenicilh.chatapp.auth.service.AuthService;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.auth.dto.response.AuthResponse;
import jakarta.validation.Valid;
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

    /**
     * Handles user registration and returns a JWT if successful.
     *
     * @param registerDtoRequest The registration details.
     * @return A ResponseEntity containing the JWT.
     * @throws UserException
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid RegisterDtoRequest registerDtoRequest) throws UserException {
        String jwt = authService.register(registerDtoRequest);
        return new ResponseEntity<>(new AuthResponse(jwt, true), HttpStatus.CREATED);
    }

    /**
     * Handles user login and returns a JWT if the credentials are valid.
     *
     * @param loginDtoRequest the login details.
     * @return A ResponseEntity containing the JWT.
     * @throws UserException
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody @Valid LoginDtoRequest loginDtoRequest) throws UserException {
        String jwt = authService.login(loginDtoRequest);
        return new ResponseEntity<>(new AuthResponse(jwt, true), HttpStatus.OK);
    }



}
