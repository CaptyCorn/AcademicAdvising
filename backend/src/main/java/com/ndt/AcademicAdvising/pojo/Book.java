/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.pojo;

import com.ndt.AcademicAdvising.enums.BookCondition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tbl_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 150, nullable = false)
    private String name;
    @Column(length = 255, nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCondition condition;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    @ManyToMany(mappedBy = "books")
    private Set<Subject> subjects;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "book")
    private Set<BookImage> bookImages;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Book() {
    }

    public Book(Integer id, String name, String description, Double price, Integer amount, BookCondition condition, Date createdAt, Date updatedAt, Set<Subject> subjects, Set<BookImage> bookImages, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.condition = condition;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.subjects = subjects;
        this.bookImages = bookImages;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return the condition
     */
    public BookCondition getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(BookCondition condition) {
        this.condition = condition;
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
     * @return the subjects
     */
    public Set<Subject> getSubjects() {
        return subjects;
    }

    /**
     * @param subjects the subjects to set
     */
    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    /**
     * @return the bookImages
     */
    public Set<BookImage> getBookImages() {
        return bookImages;
    }

    /**
     * @param bookImages the bookImages to set
     */
    public void setBookImages(Set<BookImage> bookImages) {
        this.bookImages = bookImages;
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
