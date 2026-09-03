/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.pojo.Major;
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
import com.ndt.AcademicAdvising.repositories.custom.CustomMajorRepository;

/**
 *
 * @author ngodo
 */
@Repository
public class MajorRepositoryImpl implements CustomMajorRepository{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Value("${pagination.major.size}")
    private Integer majorSize;

    @Override
    public Page<Major> getListMajor(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Major> query = builder.createQuery(Major.class);
        Root root = query.from(Major.class);
        query.orderBy(builder.asc(root.get("id")));
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(root.get("name"), String.format("%%%s%%", kw)));
            }
        }
        
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }
        
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0;
        
        TypedQuery<Major> q = entityManager.createQuery(query);
        q.setFirstResult(page * majorSize);
        q.setMaxResults(majorSize);
        
        List<Major> resultList = q.getResultList();
        
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Major.class);
        List<Predicate> countPredicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                countPredicates.add(builder.like(countRoot.get("name"), String.format("%%%s%%", kw)));
            }
        }
        countQuery.select(builder.count(countRoot));
        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(Predicate[]::new));
        }
        Long totalMajor = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(resultList, PageRequest.of(page, majorSize), totalMajor);
    }
}
