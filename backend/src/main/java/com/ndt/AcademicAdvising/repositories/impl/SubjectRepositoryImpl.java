/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.pojo.Subject;
import com.ndt.AcademicAdvising.repositories.SubjectRepository;
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
import com.ndt.AcademicAdvising.repositories.custom.CustomSubjectRepository;

/**
 *
 * @author ngodo
 */
@Repository
public class SubjectRepositoryImpl implements CustomSubjectRepository{

    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${pagination.subject.size}")
    private Integer subjectSize;
    
    @Override
    public Page<Subject> getListSubject(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
        Root root = query.from(Subject.class);
        query.select(root).distinct(true);
        query.orderBy(builder.asc(root.get("id")));
        
        List<Predicate> predicates = buildPredicate(builder, root, params);
        
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }
        
        int page = 0;
        if (params != null && params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        
        TypedQuery<Subject> q = entityManager.createQuery(query);
        q.setFirstResult(page  * subjectSize);
        q.setMaxResults(subjectSize);       
        List<Subject> resultList = q.getResultList();
        
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Subject.class);
        
        List<Predicate> countPredicates = buildPredicate(builder, countRoot, params);
        
        countQuery.select(builder.countDistinct(countRoot));
        
        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(Predicate[]::new));
        }
        Long totalSubject = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(resultList, PageRequest.of(page, subjectSize), totalSubject);
    }
    
    private List<Predicate> buildPredicate(CriteriaBuilder builder, Root<Subject> root, Map<String, String> params) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }
            
            String majorId = params.get("majorId");
            if (majorId != null && !majorId.isEmpty()) {
                predicates.add(builder.equal(root.get("major").get("id"), Integer.valueOf(majorId)));
            }
        }
        
        return predicates;
    }
    
}
