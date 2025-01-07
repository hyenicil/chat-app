package com.yenicilh.chatapp.user.service.impl;

import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.common.security.jwt.JwtService;
import com.yenicilh.chatapp.user.dto.request.UpdateUserDtoRequest;
import com.yenicilh.chatapp.user.dto.request.UserDtoRequest;
import com.yenicilh.chatapp.user.dto.response.UserAuthDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.entity.enums.Role;
import com.yenicilh.chatapp.user.mapper.UserDtoMapper;
import com.yenicilh.chatapp.user.repository.UserRepository;
import com.yenicilh.chatapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserAuthDtoResponse save(User user) {
        try {
            if(isNull(findUserByEmail(user.getEmail()))) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setAuthorities(Set.of(Role.ROLE_USER));
                userRepository.save(user);
                Set<Role> claims = new HashSet<>();
                user.getAuthorities().forEach(authority -> claims.add(Role.valueOf(authority.getAuthority())));
                return new UserAuthDtoResponse(claims, user.getUsername(), user.getEmail());
            }
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public User findUserByEmail(String email) throws UserException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent())
            return optionalUser.get();
        return null;
        //throw new UserException("User not found with email: " + email);
    }

    @Override
    public Long findUserProfile(String jwt) {
        String userName = jwtService.extractUser(jwt);
        Optional<User> optionalUser = userRepository.findByUsername(userName);
        return optionalUser.isPresent() ? optionalUser.get().getId() : null;
    }

    @Override
    public User updateUser(Long id, UpdateUserDtoRequest request) throws UserException {
        User user = findById(id);

        if(Objects.nonNull(request.email()) && Objects.nonNull(request.username())) {
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setEmail(request.profilePictureUrl());
            userRepository.save(user);
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "USER");
            claims.put("email", user.getEmail());
        }

        return user;
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;
    }

    @Override
    public User findById(Long id) {
        return userRepository.getReferenceById(id);
    }
}
