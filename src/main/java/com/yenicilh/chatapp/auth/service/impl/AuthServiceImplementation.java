package com.yenicilh.chatapp.auth.service.impl;

import com.yenicilh.chatapp.auth.dto.request.LoginDtoRequest;
import com.yenicilh.chatapp.auth.dto.request.RegisterDtoRequest;
import com.yenicilh.chatapp.auth.dto.response.AuthResponse;
import com.yenicilh.chatapp.auth.mapper.AuthDtoMapper;
import com.yenicilh.chatapp.auth.service.AuthService;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.common.security.jwt.JwtService;
import com.yenicilh.chatapp.common.security.service.CustomUserDetailsService;
import com.yenicilh.chatapp.user.dto.response.UserAuthDtoResponse;
import com.yenicilh.chatapp.user.service.UserService;
import io.jsonwebtoken.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Objects.isNull;

@Service
public class AuthServiceImplementation implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthDtoMapper authDtoMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImplementation(JwtService jwtService, UserService userService, AuthDtoMapper authDtoMapper, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authDtoMapper = authDtoMapper;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles user registration by saving the user entity and generating a JWT token.
     *
     * @param request The registration request containing user details.
     * @return The generated JWT token.
     */
    @Override
    public String register(RegisterDtoRequest request) throws UserException {
        UserAuthDtoResponse response = userService.save(authDtoMapper.toEntity(request));
        return generateToken(response);
    }

    @Override
    public String login(LoginDtoRequest request) throws UserException {
        Authentication authentication = authentication(request.email(), request.password());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateToken(authentication);
    }

    /**
     * Generates a JWT token with user claims.
     *
     * @param response The user response containing the claims and username.
     * @return The generated JWT token.
     * @throws UserException
     */
    private String generateToken(UserAuthDtoResponse response) throws UserException {
        return jwtService.generateToken(response.claims(), response.username());
    }

    private Authentication authentication(String email, String password) throws UserException {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userService.findUserByEmail(email).username());

        if(isNull(userDetails))
            throw new UserException("User not found with email: " + email);

        if(!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new UserException("Invalid password provided.");

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
