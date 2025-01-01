package com.yenicilh.chatapp.chat.repository;

import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {


    @Query("select c from Chat c join c.users u where u.id = :userId")
    List<Chat> findChatByUserId(@Param("userId") Long userId);

    @Query("select c from Chat c where c.isGroup =false and :source member of c.users and :target member of c.users")
    Chat findChatByUserIds(@Param("source") User source, @Param("target") User target);
}
