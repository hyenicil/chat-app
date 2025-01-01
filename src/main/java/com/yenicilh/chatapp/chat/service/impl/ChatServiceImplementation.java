package com.yenicilh.chatapp.chat.service.impl;

import com.yenicilh.chatapp.chat.dto.request.GroupChatDtoRequest;
import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.chat.mapper.ChatDtoMapper;
import com.yenicilh.chatapp.chat.repository.ChatRepository;
import com.yenicilh.chatapp.chat.service.ChatService;
import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.user.UserException;
import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class ChatServiceImplementation implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatDtoMapper chatDtoMapper;
    private final UserService userService;

    public ChatServiceImplementation(ChatRepository chatRepository, ChatDtoMapper chatDtoMapper, UserService userService) {
        this.chatRepository = chatRepository;
        this.chatDtoMapper = chatDtoMapper;
        this.userService = userService;
    }


    @Override
    public Chat createChat(Long sourceId, Long targetId) throws UserException {
        User source = findUserById(sourceId);
        User target = findUserById(targetId);

        Chat isChatExist = chatRepository.findChatByUserIds(source, target);
        if(nonNull(isChatExist))
            return isChatExist;

        Chat chat = new Chat();
        chat.setCreatedBy(source);
        chat.getUsers().add(target);
        chat.getUsers().add(source);
        chat.setGroup(false);
        chatRepository.save(chat);

        return chat;
    }

    @Override
    public Chat findChatById(Long id) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(id);
        if(chat.isPresent())
            return chat.get();
        throw new ChatException("Chat not found with id:" + id );
    }

    @Override
    public List<Chat> findAllChatBySourceId(Long sourceId) throws UserException {

        User user = userService.findById(sourceId);
        List<Chat> chats = chatRepository.findChatByUserId(user.getId());
        return chats;
    }

    @Override
    public Chat createGroup(GroupChatDtoRequest request, Long sourceId) throws UserException {

        Chat tempChat = chatDtoMapper.toEntity(request);
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(tempChat.getChat_image());
        group.setName(tempChat.getName());
        User user = findUserById(sourceId);
        if(nonNull(user)) {
            group.setCreatedBy(user);
            group.getAdmins().add(user);
        }
        for (User val : tempChat.getUsers())
            group.getUsers().add(findUserById(val.getId()));
        return group;
    }

    @Override
    public Chat addUserToGroup(Long chatId, Long sourceId, Long targetId) throws UserException, ChatException {
       Optional <Chat> chat = chatRepository.findById(chatId);
       User user = findUserById(targetId);
       if(chat.isPresent()) {
           if (chat.get().getAdmins().contains(findUserById(sourceId))) {
               chat.get().getUsers().add(user);
               return chat.get();
           }
           else {
               throw new UserException("You are not admin of this chat");
           }
       }
       throw new ChatException("Chat not found with id:" + chatId);
    }

    @Override
    public Chat renameGroup(Long id, String groupName, Long targetId) throws UserException, ChatException {
        Optional<Chat> chat = chatRepository.findById(id);
        if(chat.isPresent()) {
            if (chat.get().getUsers().contains(findUserById(targetId))) {
                chat.get().setName(groupName);
                return chatRepository.save(chat.get());
            }
            throw new UserException("You are not user of this chat");
        }
        throw new ChatException("Chat not found with id:" + id);
    }

    @Override
    public Chat removeFromGroup(Long id, Long sourceId, Long targetId) throws UserException, ChatException {
        Optional<Chat> chat = chatRepository.findById(id);
        if(chat.isPresent()) {
            User source = findUserById(sourceId);
            User target = findUserById(targetId);
            if(chat.get().getAdmins().contains(source)) {
                chat.get().getUsers().remove(target);
                return chatRepository.save(chat.get());
            }
            else if(chat.get().getUsers().contains(source) && source.equals(target)) {
                chat.get().getUsers().remove(target);
                return chatRepository.save(chat.get());
            }
            else {
                throw new UserException("You can not remove another user from the chat");
            }
        }
        throw new ChatException("Chat not found with id:" + id);
    }

    @Override
    public void deleteChat(Long id, Long sourceId) throws ChatException, UserException {

        Optional<Chat> chat = chatRepository.findById(id);

        if(chat.isPresent() && chat.get().getAdmins().contains(findUserById(sourceId))) {
            chatRepository.delete(chat.get());
        }
        throw new ChatException("Chat can not remove chat");
    }

    private User findUserById(Long id) throws UserException {
        User user ;
        if(nonNull(user = userService.findById(id)))
            return user;
         throw new UserException("User not found with id:" + id);
    }
}