/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories.impl;

import com.ndt.AcademicAdvising.dto.ResponseCommentDTO;
import com.ndt.AcademicAdvising.enums.UserRole;
import com.ndt.AcademicAdvising.pojo.Comment;
import com.ndt.AcademicAdvising.pojo.User;
import com.ndt.AcademicAdvising.repositories.custom.CustomCommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
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
public class CommentRepositoryImpl implements CustomCommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${pagination.comment.size}")
    private Integer commentSize;

    @Override
    public Page<Comment> getListComment(Map<String, String> params, int postId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> query = builder.createQuery(Comment.class);
        Root root = query.from(Comment.class);

        Join<Comment, User> userJoin = root.join("user", JoinType.LEFT);
        query.select(root);

        query.where(builder.equal(root.get("post").get("id"), postId));
        Expression<Integer> aiFirst = builder.<Integer>selectCase().when(
                builder.equal(
                        userJoin.get("userRole"),
                        UserRole.ROLE_AI
                ), 0).otherwise(1);
        query.orderBy(builder.asc(aiFirst), builder.desc(root.get("createdAt")));

        int page = 0;
        if (params != null && params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }

        TypedQuery<Comment> q = entityManager.createQuery(query);
        q.setFirstResult(page * commentSize);
        q.setMaxResults(commentSize);
        List<Comment> resultList = q.getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root countRoot = countQuery.from(Comment.class);

        countQuery.select(builder.countDistinct(countRoot));
        countQuery.where(builder.equal(countRoot.get("post").get("id"), postId));
        Long totalComment = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(resultList, PageRequest.of(page, commentSize), totalComment);
    }

}
