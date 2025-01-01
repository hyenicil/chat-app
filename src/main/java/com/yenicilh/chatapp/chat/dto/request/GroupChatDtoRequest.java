package com.yenicilh.chatapp.chat.dto.request;

import java.util.List;

public record GroupChatDtoRequest(

        List<Long> users,
        String chat_name,
        String chat_image
){
}
