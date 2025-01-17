package com.yenicilh.chatapp.user.service;

import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.user.dto.request.UpdateUserDtoRequest;
import com.yenicilh.chatapp.user.dto.request.UserDtoRequest;
import com.yenicilh.chatapp.user.dto.response.UserAuthDtoResponse;
import com.yenicilh.chatapp.user.dto.response.UserDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

    UserDtoResponse findUserByEmail(String email) throws UserException;
    UserDtoResponse findUserProfile(String jwt) throws UserException;
    User updateUser(Long id, UpdateUserDtoRequest request) throws UserException;
    List<User> searchUser(String query);
    UserAuthDtoResponse save(User user) throws UserException;
    User findById(Long sourceId) throws UserException;
    Long findByEmail(String email) throws UserException;
}
