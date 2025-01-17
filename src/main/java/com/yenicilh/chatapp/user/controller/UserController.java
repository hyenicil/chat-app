package com.yenicilh.chatapp.user.controller;

import com.yenicilh.chatapp.common.dto.response.ApiResponse;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.user.dto.request.UpdateUserDtoRequest;
import com.yenicilh.chatapp.user.dto.response.UserDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDtoResponse> getUserProfile(@RequestHeader("Authorization") String token) throws UserException {
        UserDtoResponse user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUser(@PathVariable("query") String query) {
        List<User> users = userService.searchUser(query);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserDtoRequest request, @RequestHeader("Authorization") String token) {
        ApiResponse apiResponse = new ApiResponse("User updated successfully", true);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.ACCEPTED);
    }



}
