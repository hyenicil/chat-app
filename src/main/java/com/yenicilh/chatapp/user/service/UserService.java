package com.yenicilh.chatapp.user.service;

import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.user.dto.request.UpdateUserDtoRequest;
import com.yenicilh.chatapp.user.dto.request.UserDtoRequest;
import com.yenicilh.chatapp.user.dto.response.UserAuthDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

    User findUserByEmail(String email) throws UserException;
    Long findUserProfile(String jwt);
    User updateUser(Long id, UpdateUserDtoRequest request) throws UserException;
    List<User> searchUser(String query);
    UserAuthDtoResponse save(User user);
    User findById(Long sourceId);
}
