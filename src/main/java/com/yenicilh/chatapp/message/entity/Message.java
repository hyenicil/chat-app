package com.yenicilh.chatapp.message.entity;


import com.yenicilh.chatapp.chat.entity.Chat;
import com.yenicilh.chatapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "messagees")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "createdTime")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name ="chat_id", nullable = false)
    private Chat chat;
}
