package com.yenicilh.chatapp.message.service.impl;

import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.chat.repository.ChatRepository;
import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.message.MessageException;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.message.dto.MessageDtoRequest;
import com.yenicilh.chatapp.message.entity.Message;
import com.yenicilh.chatapp.message.mapper.MessageDtoMapper;
import com.yenicilh.chatapp.message.repository.MessageRepository;
import com.yenicilh.chatapp.message.service.MessageService;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class MessageServiceImplementation implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    //private final MessageDtoMapper messageDtoMapper;
    private final ChatRepository chatRepository;

    public MessageServiceImplementation(MessageRepository messageRepository, UserService userService , ChatRepository chatRepository) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatRepository = chatRepository;
    }

    @Override
    public Message sendMessage(MessageDtoRequest request, Long sourceId) throws UserException, ChatException {
        return null;
    }

    @Override
    public List<Message> getChatMessages(Long chatId, Long sourceId) throws UserException, ChatException {
        return List.of();
    }

    @Override
    public Message findMessageById(Long id) throws MessageException {
        return null;
    }

    @Override
    public void deleteMessage(Long id, Long sourceId) throws UserException, MessageException {

    }
/*
    @Override
    public Message sendMessage(MessageDtoRequest request, Long sourceId) throws UserException, ChatException {
        Message message = messageDtoMapper.toEntity(request);
        message.setUser(userService.findById(sourceId));
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatMessages(Long chatId, Long sourceId) throws UserException, ChatException {
        Chat chat = chatRepository.findById(chatId).get();
        User user = findUserById(sourceId);

        if(!chat.getUsers().contains(user))
            throw new UserException("You can't view these messagesthis chat");

        List<Message> messages = messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Long id) throws MessageException {
        Optional<Message> message = messageRepository.findById(id);
        if(message.isPresent())
            return message.get();
        throw new MessageException("Message not found id: "+ id);
    }

    @Override
    public void deleteMessage(Long id, Long sourceId) throws  UserException, MessageException {
        Optional<Message> message = messageRepository.findById(id);
        if(message.get().getUser().equals(findUserById(sourceId)))
            messageRepository.delete(message.get());
        throw new MessageException("Message can not delete id: "+ id);
    }

    private User findUserById(Long id) throws UserException {
        User user ;
        if(nonNull(user = userService.findById(id)))
            return user;
        throw new UserException("User not found with id:" + id);
    }*/
}
