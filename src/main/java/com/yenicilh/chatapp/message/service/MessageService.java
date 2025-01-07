package com.yenicilh.chatapp.message.service;

import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.message.MessageException;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.message.dto.MessageDtoRequest;
import com.yenicilh.chatapp.message.entity.Message;
import com.yenicilh.chatapp.message.repository.MessageRepository;
import com.yenicilh.chatapp.user.entity.User;

import java.util.List;

public interface MessageService {

    Message sendMessage(MessageDtoRequest request, Long sourceId) throws UserException, ChatException;

    List<Message> getChatMessages(Long chatId, Long sourceId) throws UserException, ChatException;

    Message findMessageById(Long id) throws MessageException;

    void deleteMessage(Long id, Long sourceId) throws UserException, MessageException;


}
