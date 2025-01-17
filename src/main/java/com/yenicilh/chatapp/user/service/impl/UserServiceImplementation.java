package com.yenicilh.chatapp.user.service.impl;

import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.common.security.jwt.JwtService;
import com.yenicilh.chatapp.user.dto.request.UpdateUserDtoRequest;
import com.yenicilh.chatapp.user.dto.response.UserAuthDtoResponse;
import com.yenicilh.chatapp.user.dto.response.UserDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.entity.enums.Role;
import com.yenicilh.chatapp.user.mapper.UserDtoMapper;
import com.yenicilh.chatapp.user.repository.UserRepository;
import com.yenicilh.chatapp.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImplementation(UserRepository userRepository, UserDtoMapper userDtoMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    public UserDtoResponse findUserByEmail(String email) throws UserException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            return null;
        return userDtoMapper.toResponse(optionalUser.get());
    }

    @Override
    public UserDtoResponse findUserProfile(String jwt) throws UserException {
        String username = jwtService.extractUsername(jwt);
        return userRepository.findByUsername(username)
                .map(userDtoMapper::toResponse)
                .orElseThrow(() -> new UserException("User not found with username: " + username));
    }

    @Override
    public User updateUser(Long id, UpdateUserDtoRequest request) throws UserException {

        User user = findById(id);
        //Burada daha sonra swap kullanilabilir.
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.email() != null) {
            if (findUserByEmail(request.email()) != null)
                throw new UserException("Email is already in use");
            user.setEmail(request.email());
        }
        if (request.profilePictureUrl() != null) {
            user.setProfilePictureUrl(request.profilePictureUrl());
        }
        userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("email", user.getEmail());
        return user;
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;    }

    @Override
    public UserAuthDtoResponse save(User user) throws UserException {

        if (isNull(user.getEmail()) || isNull(user.getPassword()))
            throw new UserException("Email or Password cannot be null");

        if (findUserByEmail(user.getEmail()) != null)
            throw new UserException("User with this email already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(Set.of(Role.USER));
        userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        user.getAuthorities().forEach(authority -> claims.put(Role.valueOf(authority.getAuthority()).toString(), authority));
        return new UserAuthDtoResponse(claims, user.getUsername(), user.getEmail());
    }

    @Override
    public User findById(Long id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with ID: " + id));
    }

    @Override
    public Long findByEmail(String email) throws UserException {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UserException("User not found with email: " + email));
    }
}
