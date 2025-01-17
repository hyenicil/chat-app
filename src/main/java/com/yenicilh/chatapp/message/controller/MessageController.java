package com.yenicilh.chatapp.message.controller;

import com.yenicilh.chatapp.common.dto.response.ApiResponse;
import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.message.MessageException;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.message.dto.MessageDtoRequest;
import com.yenicilh.chatapp.message.entity.Message;
import com.yenicilh.chatapp.message.service.MessageService;
import com.yenicilh.chatapp.user.dto.response.UserDtoResponse;
import com.yenicilh.chatapp.user.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/chat-app/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDtoRequest request, @RequestHeader("Authorization") String token) throws UserException, ChatException {
        Message message =  messageService.sendMessage(request, findUserProfile(token));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @GetMapping("/chat/{id}")
    public ResponseEntity<List<Message>> getChatMessage(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) throws UserException, ChatException {
        List<Message> messageList = messageService.getChatMessages(id,findUserProfile(token));
        return new ResponseEntity<List<Message>>(messageList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@RequestHeader("Authorization") String token, @Param("id") Long id) throws UserException, ChatException, MessageException {
        messageService.deleteMessage(findUserProfile(token), id);
        ApiResponse apiResponse = new ApiResponse("Message is deleted.", true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private Long findUserProfile(String jwt) throws UserException {
        UserDtoResponse user = userService.findUserProfile(jwt);
        if(nonNull(user)) {
            return userService.findByEmail(user.email());
        }
        throw new UserException("User not found");
    }
}
