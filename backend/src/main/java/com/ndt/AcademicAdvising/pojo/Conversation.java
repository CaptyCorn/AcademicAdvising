/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.pojo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */

@Entity
@Table(name = "tbl_conversation")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "last_message", length = 255)
    private String lastMessage;
    @Column(name = "last_message_time")
    private Date lastMessageTime;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    @ManyToMany(mappedBy = "conversations")
    private Set<User> users;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "conversation")
    private Set<Message> messages;

    public Conversation() {
    }

    public Conversation(Integer id, String lastMessage, Date lastMessageTime, Date createdAt, Date updatedAt, Set<User> users, Set<Message> messages) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.users = users;
        this.messages = messages;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the lastMessage
     */
    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * @param lastMessage the lastMessage to set
     */
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    /**
     * @return the lastMessageTime
     */
    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * @param lastMessageTime the lastMessageTime to set
     */
    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the updatedAt
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * @return the messages
     */
    public Set<Message> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
    
    
}
