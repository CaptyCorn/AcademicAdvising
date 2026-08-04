/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.pojo;

import com.ndt.AcademicAdvising.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */

@Entity
@Table(name = "tbl_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(length = 1000, nullable = false)
    private String link;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Notification() {
    }

    public Notification(Integer id, String title, String content, NotificationType type, Boolean isRead, String link, Date createdAt, Date updatedAt, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.link = link;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * @return the isRead
     */
    public Boolean getIsRead() {
        return isRead;
    }

    /**
     * @param isRead the isRead to set
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
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
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    
}
