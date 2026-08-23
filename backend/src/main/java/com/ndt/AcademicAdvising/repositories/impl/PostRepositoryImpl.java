/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.dto.ResponsePostDTO;
import com.ndt.AcademicAdvising.dto.ResponseUserDTO;
import com.ndt.AcademicAdvising.pojo.Comment;
import com.ndt.AcademicAdvising.pojo.Post;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.custom.CustomPostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
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

/**
 *
 * @author ngodo
 */
public class PostRepositoryImpl implements CustomPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${pagination.post.size}")
    private Integer postSize;

    @Override
    public Page<ResponsePostDTO> getListPost(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        int page = 0;
        if (params != null && params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }

        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root postRoot = query.from(Post.class);

        Join<Post, User> userJoin = postRoot.join("user", JoinType.INNER);
        Join<Post, Comment> commentJoin = postRoot.join("comments", JoinType.LEFT);

        List<Predicate> predicates = buildPredicate(builder, postRoot, params);

        query.select(
                builder.tuple(
                        postRoot.get("id").alias("id"),
                        postRoot.get("content").alias("content"),
                        postRoot.get("createdAt").alias("createdAt"),
                        userJoin.get("firstName").alias("firstName"),
                        userJoin.get("lastName").alias("lastName"),
                        userJoin.get("username").alias("username"),
                        userJoin.get("email").alias("email"),
                        userJoin.get("avatar").alias("avatar"),
                        userJoin.get("studentCode").alias("studentCode"),
                        builder.count(commentJoin.get("id")).alias("commentCount")
                )
        );

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(Predicate[]::new));
        }

        query.groupBy(
                postRoot.get("id"),
                postRoot.get("content"),
                postRoot.get("createdAt"),
                userJoin.get("firstName"),
                userJoin.get("lastName"),
                userJoin.get("username"),
                userJoin.get("email"),
                userJoin.get("avatar"),
                userJoin.get("studentCode")
        );

        query.orderBy(builder.desc(postRoot.get("createdAt")));

        TypedQuery<Tuple> q = entityManager.createQuery(query);
        q.setFirstResult(page * postSize);
        q.setMaxResults(postSize);
        List<Tuple> resultList = q.getResultList();

        List<ResponsePostDTO> content = resultList.stream().map(this::toDTO).toList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Post.class);

        List<Predicate> countPredicates = buildPredicate(builder, countRoot, params);

        countQuery.select(builder.countDistinct(countRoot));

        if (!countPredicates.isEmpty()) {
            countQuery.where(countPredicates.toArray(Predicate[]::new));
        }
        Long totalSubject = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(content, PageRequest.of(page, postSize), totalSubject);
    }

    private List<Predicate> buildPredicate(CriteriaBuilder builder, Root<Post> root, Map<String, String> params) {
        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("content")), '%' + kw.toLowerCase() + '%'));
            }
        }
        return predicates;
    }

    private ResponsePostDTO toDTO(Tuple tuple) {
        String firstName = tuple.get("firstName", String.class);
        String lastName = tuple.get("lastName", String.class);

        ResponseUserDTO user
                = new ResponseUserDTO(
                        lastName + " " + firstName,
                        tuple.get("username", String.class),
                        tuple.get("email", String.class),
                        tuple.get("avatar", String.class),
                        tuple.get("studentCode", String.class)
                );

        ResponsePostDTO dto = new ResponsePostDTO();

        dto.setId(tuple.get("id", Integer.class));
        dto.setContent(tuple.get("content", String.class));
        dto.setCreatedAt(tuple.get("createdAt", java.util.Date.class));
        dto.setUser(user);
        dto.setCommentCount(tuple.get("commentCount", Long.class));

        return dto;
    }
}
