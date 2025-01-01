package com.yenicilh.chatapp.chat.entity;

import com.yenicilh.chatapp.message.entity.Message;
import com.yenicilh.chatapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JoinColumn(nullable = false, name = "admins")
    @ManyToMany
    private Set<User> admins = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "chat_image")
    private String chat_image;

    @Column(name="is_group")
    private boolean isGroup;

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @ManyToMany
    @JoinColumn(name = "members")
    private Set<User> users = new HashSet<>();

    @OneToMany
    @JoinColumn(name ="messages", nullable = false)
    private List<Message> messages = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<User> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<User> admins) {
        this.admins = admins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChat_image() {
        return chat_image;
    }

    public void setChat_image(String chat_image) {
        this.chat_image = chat_image;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


}
