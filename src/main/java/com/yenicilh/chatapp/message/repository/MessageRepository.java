package com.yenicilh.chatapp.message.repository;

import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("Select m From Message m join m.chat c where c.id=:chatId")
    public List<Message> findByChatId(@Param("chatId") Long chatId);
}
