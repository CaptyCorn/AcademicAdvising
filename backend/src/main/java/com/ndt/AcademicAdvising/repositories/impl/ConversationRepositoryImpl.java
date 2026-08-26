/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.pojo.Conversation;
import com.ndt.AcademicAdvising.repositories.custom.CustomConversationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public class ConversationRepositoryImpl implements CustomConversationRepository{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${pagination.conversation.size}")
    private int conversationSize;

    @Override
    public Page<Conversation> getListConversation(Map<String, String> params, int userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Conversation> query = builder.createQuery(Conversation.class);
        Root root = query.from(Conversation.class);
        
        List<Predicate> predicates = buildPredicate(builder, root, params, userId);
        query.where(predicates.toArray(Predicate[]::new));
        
        query.orderBy(builder.desc(root.get("lastMessageTime")), builder.desc(root.get("updatedAt")));
        
        int page = 0;
        if (params != null && params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }

        TypedQuery<Conversation> q = entityManager.createQuery(query);
        q.setFirstResult(page * conversationSize);
        q.setMaxResults(conversationSize);
        List<Conversation> resultList = q.getResultList();
        
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Conversation.class);
        
        List<Predicate> countPredicates = buildPredicate(builder, countRoot, params, userId);
        countQuery.select(builder.count(countRoot));
        
        countQuery.where(countPredicates.toArray(Predicate[]::new));       
        Long totalConversation = entityManager.createQuery(countQuery).getSingleResult();
               
        return new PageImpl<>(resultList, PageRequest.of(page, conversationSize), totalConversation);      
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder builder, Root<Conversation> root, Map<String, String> params, int userId) {
        List<Predicate> predicates = new ArrayList<>();
        
        Predicate senderCondition = builder.equal(root.get("sender").get("id"), userId);
        Predicate receiverCondition = builder.equal(root.get("receiver").get("id"), userId);
        
        predicates.add(builder.or(senderCondition, receiverCondition));
        
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                Predicate senderSearch = builder.like(builder.lower(root.get("sender").get("username")), "%" + kw.toLowerCase() + "%");
                Predicate receiverSearch = builder.like(builder.lower(root.get("receiver").get("username")), "%" + kw.toLowerCase() + "%");
                predicates.add(builder.or(senderSearch, receiverSearch));
            }
        }      
        return predicates;
    }
    
}
