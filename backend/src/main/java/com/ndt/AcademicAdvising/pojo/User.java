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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "tbl_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 100, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(name = "student_code", length = 15, nullable = false, unique = true)
    private String studentCode;
    @Column(length = 11, unique = true)
    private String phone;
    @Column(length = 150)
    private String avatar;
    @Column(name = "avatar_public_id", unique = true)
    private String avatarPublicId;
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
    
    @OneToMany(mappedBy = "buyer")
    private Set<Payment> buyingPayments;
    @OneToMany(mappedBy = "seller")
    private Set<Payment> sellingPayments;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "sender")
    private Set<Notification> sendingNotifications;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "receiver")
    private Set<Notification> receivingNotifications;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "last_sender")
    private Set<Conversation> conversations;
    
    @OneToMany(mappedBy = "sender")
    private Set<Message> messages;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    private Set<Report> reports;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private Set<Post> posts;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private Set<Comment> comments;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    private Set<CommentReaction> commentReactions;

    @Transient
    public String getName() {
        return lastName + " " + firstName;
    }
}
