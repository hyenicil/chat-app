package com.yenicilh.chatapp.chat.controller;

import com.yenicilh.chatapp.chat.dto.request.ChatDtoRequest;
import com.yenicilh.chatapp.chat.dto.request.GroupChatDtoRequest;
import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.chat.service.ChatService;
import com.yenicilh.chatapp.common.dto.response.ApiResponse;
import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.user.dto.response.UserDtoResponse;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.LockInfo;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/chat-app/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/single")
    public ResponseEntity<Chat> createChat(@RequestBody ChatDtoRequest request, @RequestHeader("Authorization") String jwt) throws UserException {
        Chat chat = chatService.createChat(findUserProfile(jwt), request.userId());
        return new ResponseEntity<Chat>(chat, HttpStatus.CREATED);
    }

    @PostMapping("/groups")
    public ResponseEntity<Chat> createChatGroup(@RequestBody GroupChatDtoRequest request, @RequestHeader("Authorization") String jwt) throws UserException {
        Chat chat = chatService.createGroup(request, findUserProfile(jwt));
        return new ResponseEntity<Chat>(chat, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChat(@PathVariable Long id,@RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        Chat chat = chatService.findChatById(id);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> getChatByUser(@RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        List<Chat> chats = chatService.findAllChatBySourceId(findUserProfile(jwt));
        return new ResponseEntity<List<Chat>>(chats,HttpStatus.OK);
    }

    @PutMapping("/{id}/add-user/{userId}")
    public ResponseEntity<Chat> addUserToChat(@PathVariable Long id, @PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        Chat chat = chatService.addUserToGroup( id, findUserProfile(jwt), userId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PutMapping("/{id}/remove-user/{userId}")
    public ResponseEntity<Chat> removeUserToChat(@PathVariable Long id, @PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        Chat chat = chatService.removeFromGroup(id,findUserProfile(jwt), userId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        Long userId = findUserProfile(jwt);
        chatService.deleteChat(id, findUserProfile(jwt));
        ApiResponse apiResponse = new ApiResponse("Chat is deleted.", true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    private Long findUserProfile(String jwt) throws UserException {
        UserDtoResponse user = userService.findUserProfile(jwt);
        if(nonNull(user))
            return userService.findByEmail(user.email());
        throw new UserException("User not found");
    }
}
