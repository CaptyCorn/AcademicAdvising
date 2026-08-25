/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.repositories;

import com.ndt.AcademicAdvising.pojo.Comment;
import com.ndt.AcademicAdvising.repositories.custom.CustomCommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ngodo
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>, CustomCommentRepository{
    public Page<Comment> findAllByPostIdOrderByCreatedAtDesc(int postId, Pageable pageable);
}
