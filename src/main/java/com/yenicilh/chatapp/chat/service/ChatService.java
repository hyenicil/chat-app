package com.yenicilh.chatapp.chat.service;

import com.yenicilh.chatapp.chat.dto.request.GroupChatDtoRequest;
import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.user.UserException;

import java.util.List;

public interface ChatService {

    Chat createChat(Long sourceId, Long targetId) throws UserException;

    Chat findChatById(Long id) throws ChatException;

    List<Chat> findAllChatBySourceId(Long sourceId) throws UserException;

    //Burayi daha sonra kontrol edicem
    Chat createGroup(GroupChatDtoRequest request, Long sourceId)throws UserException;

    Chat addUserToGroup(Long sourceId, Long chatId, Long targetId) throws UserException, ChatException;

    Chat renameGroup(Long chatId, String groupName, Long targetId) throws UserException, ChatException;

    Chat removeFromGroup(Long chatId, Long sourceId, Long targetId) throws UserException, ChatException;

    void deleteChat(Long chatId, Long sourceId) throws ChatException, UserException;

}
