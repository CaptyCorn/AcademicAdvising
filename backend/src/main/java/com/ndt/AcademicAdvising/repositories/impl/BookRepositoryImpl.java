/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.pojo.Book;
import com.ndt.AcademicAdvising.pojo.Subject;
import com.ndt.AcademicAdvising.repositories.custom.CustomBookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
public class BookRepositoryImpl implements CustomBookRepository{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${pagination.book.size}")
    private Integer bookSize;

    @Override
    public Page<Book> getListBook(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root root = query.from(Book.class);
        
        query.select(root).distinct(true);
        query.orderBy(builder.desc(root.get("createdAt")), builder.desc(root.get("id")));
        
        List<Predicate> predicates = buildPredicate(builder, root, params);
        
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }
        
        int page = 0;
        if (params != null && params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        
        
        TypedQuery<Book> q = entityManager.createQuery(query);
        q.setFirstResult(page * bookSize);
        q.setMaxResults(bookSize);        
        List<Book> resultList = q.getResultList();
        
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Book.class);
        
        List<Predicate> countPredicates = buildPredicate(builder, countRoot, params);
        
        countQuery.select(builder.countDistinct(countRoot));
        
        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(Predicate[]::new));
        }
        Long totalBook = entityManager.createQuery(countQuery).getSingleResult();
       
        return new PageImpl<>(resultList, PageRequest.of(page, bookSize), totalBook);
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder builder, Root<Book> root, Map<String, String> params) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("name")), String.format("%%%s%%", kw.toLowerCase())));
            }
            
            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                Join<Book, Subject> subjectJoin = root.join("subjects", JoinType.INNER);
                predicates.add(builder.equal(subjectJoin.get("id"), Integer.valueOf(subjectId)));
            }

            String majorId = params.get("majorId");
            if (majorId != null && !majorId.isEmpty()) {
                Join<Book, Subject> subjectJoin = root.join("subjects", JoinType.INNER);
                predicates.add(builder.equal(subjectJoin.get("major").get("id"), Integer.valueOf(majorId)));
            }
        }
        return predicates;
    }

    @Override
    public Page<Book> getListBookById(int userId, Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root root = query.from(Book.class);
        
        query.select(root).distinct(true);
        query.orderBy(builder.asc(root.get("id")));
        
        List<Predicate> predicates = buildPredicate(builder, root, params);
        predicates.add(builder.equal(root.get("user").get("id"), userId));
        
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }
        
        int page = 0;
        if (params != null && params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        
        
        TypedQuery<Book> q = entityManager.createQuery(query);
        q.setFirstResult(page * bookSize);
        q.setMaxResults(bookSize);        
        List<Book> resultList = q.getResultList();
        
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Book.class);
        
        List<Predicate> countPredicates = buildPredicate(builder, countRoot, params);
        
        countQuery.select(builder.countDistinct(countRoot));
        
        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(Predicate[]::new));
        }
        Long totalBook = entityManager.createQuery(countQuery).getSingleResult();
       
        return new PageImpl<>(resultList, PageRequest.of(page, bookSize), totalBook);
    }
}
