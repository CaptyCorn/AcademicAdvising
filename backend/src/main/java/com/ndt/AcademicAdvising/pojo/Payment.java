/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.pojo;

import com.ndt.AcademicAdvising.enums.PaymentMethod;
import com.ndt.AcademicAdvising.enums.PaymentStatus;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */

@Entity
@Table(name = "tbl_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Double price;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    @Column(name = "transaction_code",length = 255)
    private String transactionCode;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @OneToOne
    private Book book;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Payment() {
    }

    public Payment(Integer id, Double price, PaymentMethod paymentMethod, PaymentStatus paymentStatus, String transactionCode, Date createdAt, Date updatedAt, Book book, User user) {
        this.id = id;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionCode = transactionCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.book = book;
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
     * @return the paymentMethod
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the paymentStatus
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus the paymentStatus to set
     */
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return the transactionCode
     */
    public String getTransactionCode() {
        return transactionCode;
    }

    /**
     * @param transactionCode the transactionCode to set
     */
    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
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
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * @param book the book to set
     */
    public void setBook(Book book) {
        this.book = book;
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
