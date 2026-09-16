/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.pojo.Message;
import com.ndt.AcademicAdvising.repositories.custom.CustomMessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author ngodo
 */
public class MessageRepositoryImpl implements CustomMessageRepository{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${pagination.message.size}")
    private int messageSize;

    @Override
    public Page<Message> getListMessage(Map<String, String> param, int conversationId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> query = builder.createQuery(Message.class);
        Root root = query.from(Message.class);
        
        query.where(builder.equal(root.get("conversation").get("id"), conversationId));
        
        query.orderBy(builder.desc(root.get("createdAt")));
        
        int page = 0;
        if (param != null && param.get("page") != null) {
            page = Integer.parseInt(param.get("page"));
        }
        
        TypedQuery<Message> q = entityManager.createQuery(query);
        q.setFirstResult(page * messageSize);
        q.setMaxResults(messageSize);
        List<Message> resultList = q.getResultList();
        
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Message.class);
        
        countQuery.select(builder.count(countRoot));

        countQuery.where(builder.equal(countRoot.get("conversation").get("id"), conversationId));
        Long totalConversation = entityManager.createQuery(countQuery).getSingleResult();
               
        return new PageImpl<>(resultList, PageRequest.of(page, messageSize), totalConversation);
    }
    
}
