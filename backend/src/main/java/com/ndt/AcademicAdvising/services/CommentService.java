/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ndt.AcademicAdvising.services;

import com.ndt.AcademicAdvising.dto.ResponseCommentDTO;
import com.ndt.AcademicAdvising.pojo.Comment;
import org.springframework.data.domain.Page;

/**
 *
 * @author ngodo
 */
public interface CommentService {
    Comment getComment(int postId);
    Page<ResponseCommentDTO> getComments (int postId);
    ResponseCommentDTO addComment(String content, int postId);
    ResponseCommentDTO updateComment(int commentId, String content);
    void deleteComment(int commentId);
}
