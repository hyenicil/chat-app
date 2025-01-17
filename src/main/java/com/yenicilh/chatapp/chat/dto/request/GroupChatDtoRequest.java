package com.yenicilh.chatapp.chat.dto.request;

import com.yenicilh.chatapp.user.entity.User;

import java.util.List;
import java.util.Set;

public record GroupChatDtoRequest(

        Set<User> users,
        String chat_name,
        String chat_image
){
}
