/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.pojo;

import com.ndt.AcademicAdvising.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "tbl_user")
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 255, nullable = false)
    private String name;
    @Column(name = "student_code", length = 15, nullable = false)
    private String studentCode;
    @Column(length = 100)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 100, nullable = false)
    private String email;
    @Column(length = 11, nullable = false)
    private String phone;
    @Column(length = 100)
    private String avatar;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private Set<Book> books;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Payment> payments;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private Set<Notification> notifications;
    @JoinTable(
            name = "tbl_user_conversation",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    )
    @ManyToMany
    private Set<Conversation> conversations;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private Set<Message> messages;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Report> reports;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Post> posts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments;

    public User() {
    }

    public User(Integer id, String name, String studentCode, String username, String password, String email, String phone, String avatar, UserRole userRole, Date createdAt, Date updatedAt, Set<Book> books, Set<Payment> payments, Set<Notification> notifications, Set<Conversation> conversations, Set<Message> messages, Set<Report> reports, Set<Post> posts, Set<Comment> comments) {
        this.id = id;
        this.name = name;
        this.studentCode = studentCode;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.books = books;
        this.payments = payments;
        this.notifications = notifications;
        this.conversations = conversations;
        this.messages = messages;
        this.reports = reports;
        this.posts = posts;
        this.comments = comments;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the studentCode
     */
    public String getStudentCode() {
        return studentCode;
    }

    /**
     * @param studentCode the studentCode to set
     */
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * @return the userRole
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
     * @return the books
     */
    public Set<Book> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    /**
     * @return the payments
     */
    public Set<Payment> getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    /**
     * @return the notifications
     */
    public Set<Notification> getNotifications() {
        return notifications;
    }

    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    /**
     * @return the conversations
     */
    public Set<Conversation> getConversations() {
        return conversations;
    }

    /**
     * @param conversations the conversations to set
     */
    public void setConversations(Set<Conversation> conversations) {
        this.conversations = conversations;
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

    /**
     * @return the reports
     */
    public Set<Report> getReports() {
        return reports;
    }

    /**
     * @param reports the reports to set
     */
    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    /**
     * @return the posts
     */
    public Set<Post> getPosts() {
        return posts;
    }

    /**
     * @param posts the posts to set
     */
    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    /**
     * @return the comments
     */
    public Set<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
    
    
    
}
